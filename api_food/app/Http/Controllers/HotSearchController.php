<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\HotSearch;
use Illuminate\Support\Str;
use Carbon\Carbon;

class HotSearchController extends Controller
{
    //
    function create(Request $request){
        try{
            $text_search = $request->text_search;
        
            $existingSearch = HotSearch::where('text_search', $text_search)->first();
        
            if ($existingSearch) {
                $existingSearch->number_search += 1;
                $existingSearch->save();
                return response()->json(['message' => 'update hot search successful'], 200);
            } else {
                $newSearch = new HotSearch();
                $newSearch->text_search = $text_search;
                $newSearch->number_search = 1;
                $newSearch->save();
                return response()->json(['message' => 'add new hot search successful'], 200);
            }
        }
        catch (\Exception $e) {
            return response()->json(['message' => 'Error'], 500);
        }
    } 
    
    public function getHotSearch($textSearch)
{
    $hotSearches = HotSearch::where('id', 'like', "%$textSearch%")
                             ->orWhere('text_search', 'like', "%$textSearch%")
                             ->get();

    if ($hotSearches->isNotEmpty()) {
        return response()->json($hotSearches, 200);
    } else {
        return response()->json(['message' => 'No hot search exists with the given criteria'], 404);
    }
}

public function deleteHotSearch($id)
{
    $hotSearch = HotSearch::find($id);

    if (!$hotSearch) {
        return response()->json(['message' => 'Hot search not found'], 404);
    }

    $hotSearch->delete();

    return response()->json(['message' => 'Hot search deleted successfully'], 200);
}

    public function getAllSearch(){
        $searchs = HotSearch::get();
        if ($searchs) {
            // Nếu tài khoản tồn tại, trả về thông báo rằng tài khoản đã tồn tại
            return response()->json($searchs,200);
        } else {
            // Nếu tài khoản không tồn tại, trả về thông báo rằng tài khoản không tồn tại
            return response()->json(['message' => 'Customer not found'], 404);
        }
    }
    function getTop5HotSearches()
{
    // Lấy thời gian hiện tại
    $currentTime = Carbon::now();

    // Tính thời gian 5 ngày trước
    $fiveDaysAgo = $currentTime->subDays(5);

    // Lấy danh sách các từ khóa được cập nhật trong vòng 5 ngày gần đây
    $recentHotSearches = HotSearch::where('update_time', '>=', $fiveDaysAgo)->get();

    // Mảng để lưu trữ số lần tìm kiếm của các từ khóa
    $searches = [];

    // Duyệt qua các từ khóa gần đây
    foreach ($recentHotSearches as $hotSearch) {
        // Chuẩn hóa từ khóa: chuyển đổi sang chữ thường, loại bỏ dấu và ký tự đặc biệt
        $normalizedSearch = Str::lower(preg_replace('/[^a-zA-Z0-9 ]/', '', $hotSearch->text_search));

        // Tách từ khóa thành các từ riêng biệt
        $keywords = explode(' ', $normalizedSearch);

        // Lặp qua từng từ trong từ khóa theo thứ tự từ dài đến ngắn
        $currentKeyword = '';
        foreach ($keywords as $keyword) {
            // Tạo từ khóa mới bằng cách nối từ hiện tại với từ mới 
            $currentKeyword = $currentKeyword ? $currentKeyword . ' ' . $keyword : $keyword; //nếu currentKeyword không rỗng thì thêm dấu ' '

            // Kiểm tra xem từ khóa đã tồn tại trong mảng $searches chưa
            if (!array_key_exists($currentKeyword, $searches)) {
                // Nếu chưa tồn tại, thêm từ khóa vào mảng $searches với số lần tìm kiếm ban đầu
                $searches[$currentKeyword] = $hotSearch->number_search;
            } else {
                // Nếu tồn tại, tăng số lần tìm kiếm của từ khóa đó
                $searches[$currentKeyword] += $hotSearch->number_search;
            }
        }
    }

    // Sắp xếp mảng $searches theo số lần tìm kiếm giảm dần
    arsort($searches);

    // Lấy ra top 5 từ khóa
    $top5Searches = array_slice($searches, 0, 5, true);

    return $top5Searches;
}
}
