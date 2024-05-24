<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Favotite;
use App\Models\Product;
use App\Models\OrderDetail;


class FavoriteController extends Controller
{
    //

    public function index($customer_id,$product_id){ // kiểm tra món ăn tồn tại chưa
        $favorite = Favotite::where('customer_id', $customer_id)
            ->where('product_id', $product_id)
            ->first();
        if ($favorite) {
            return response()->json(['message' => 'Product already exists'], 200);
        } else {
            return response()->json(['message' => 'Product does not exist'], 404);
        }
    }
    public function getProductInFavorite($customer_id){
        // $favorite = Favotite::where('customer_id', $request->customer_id)
        //     ->get();
        // if ($favorite) {
        //     return response()->json(['message' => $favorite], 200);
        // } else {
        //     return response()->json(['message' => 'Product not found'], 404);
        // }
        try{
            $productIds = Favotite::where('customer_id', $customer_id)
                                ->pluck('product_id')->toArray();
            // Lấy các sản phẩm chỉ từ danh sách product_id đã được sắp xếp
            $products = Product::with('review', 'image', 'categorie')
                        ->whereIn('id', $productIds)
                        ->get();
            
            // Tính điểm đánh giá trung bình cho từng sản phẩm
            foreach ($products as $product) {
                $totalRate = 0;
                $reviewCount = $product->review->count();
                // Tính tổng số điểm đánh giá của sản phẩm
                foreach ($product->review as $review) {
                    $totalRate += $review->rate;
                }
                // Nếu có đánh giá, tính toán giá trị trung bình
                if ($reviewCount > 0) {
                    $averageRate = $totalRate / $reviewCount;
                    $product->average_rate = $averageRate;
                } else {
                    // Nếu không có đánh giá, đặt giá trị trung bình là 0
                    $product->average_rate = 0;
                }
                $productId = $product->id;
                $saleNumber = OrderDetail::where('product_id', $productId)->count();
                $product->sale_number = $saleNumber;
            }
            
            // Trả về danh sách sản phẩm đã sắp xếp theo số lượng bán hàng và thông tin cần thiết
            return response()->json($products);
            
        } catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    public function addNewFavorite(Request $request){
        try{
            $favorite = new Favotite();
            $favorite->product_id = $request -> product_id;
            $favorite->customer_id = $request -> customer_id;
            $favorite->save();
            return response()->json(['message' => 'Add product to favorite successfully'], 200);
        }catch (\Exception $e){
            return response()->json(["message"=> "Add product to favorite fail"], 400);
        }
    }

    public function destroy(Request $request){
        try{
            $favorite = Favotite::where('customer_id',$request->customer_id)
                        ->where('product_id', $request->product_id)
                        ->first();
            if(!$favorite){
                return response()->json(['message'=> 'Product not found'],404);
            }
            $favorite->delete();
            return response()->json(['message'=> 'Product delete successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to delete product'],500);
        }
    }
}
