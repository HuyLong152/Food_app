<?php

namespace App\Http\Controllers;


use App\Models\OrderDetail;
use Illuminate\Http\Request;
use Illuminate\Support\Carbon;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\DB;
use App\Models\Product;
use App\Models\HotSearch;
use Illuminate\Support\Str;


// use Carbon\Carbon;



class ProductController extends Controller
{


    function getFirst10DistinctProductIDs()
{
    $first10DistinctProductIDs = OrderDetail::orderByDesc('id') 
        ->pluck('product_id')
        ->unique()
        ->take(10); 

    return $first10DistinctProductIDs;
}

    
    
    function getTop10ProductJustSold(){
        $listIds = ProductController::getFirst10DistinctProductIDs();
        $topProducts = [];
        foreach ($listIds as $id) {
            $products = Product::with('review','image','categorie')->find($id);
            $totalRate = 0;
            $reviewCount = $products->review->count();
            // Tính tổng số điểm đánh giá của sản phẩm
            foreach ($products->review as $review) {
                $totalRate += $review->rate;
            }
            $saleNumber = OrderDetail::where('product_id', $id)->count();
                $products->sale_number = $saleNumber;
            // Nếu có đánh giá, tính toán giá trị trung bình
            if ($reviewCount > 0) {
                $averageRate = $totalRate / $reviewCount;
                $products->average_rate = $averageRate;
            } else {
                // Nếu không có đánh giá, đặt giá trị trung bình là 0
                $products->average_rate = 0;
            }
            if ($products !== null) {
                // Thêm sản phẩm vào danh sách top sản phẩm
                $topProducts[] = $products;
            }
        }
        return response()->json($topProducts);
        
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

    public function index($id = null){ // lấy full product hoặc dựa vào id

        if ($id === null) {
            $products = Product::with('review','image','categorie')->get();

            // Trả về tất cả sản phẩm nếu không có ID được cung cấp
            // Tính toán giá trị trung bình của mỗi sản phẩm dựa trên đánh giá
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
        } 
        else {
            // Trả về sản phẩm theo ID nếu có ID được cung cấp
            $products = Product::with('review','image','categorie')->find($id);
            // Kiểm tra xem sản phẩm có tồn tại không
            if ($products === null) {
                // Nếu không tìm thấy sản phẩm, trả về một thông báo
                return response()->json(['message' => 'Product not found'], 404);
            }
            // Tính toán giá trị trung bình của sản phẩm dựa trên đánh giá
            $totalRate = 0;
            $reviewCount = $products->review->count();
            // Tính tổng số điểm đánh giá của sản phẩm
            foreach ($products->review as $review) {
                $totalRate += $review->rate;
            }
            $saleNumber = OrderDetail::where('product_id', $id)->count();
                $products->sale_number = $saleNumber;
            // Nếu có đánh giá, tính toán giá trị trung bình
            if ($reviewCount > 0) {
                $averageRate = $totalRate / $reviewCount;
                $products->average_rate = $averageRate;
            } else {
                // Nếu không có đánh giá, đặt giá trị trung bình là 0
                $products->average_rate = 0;
            }
        }
        return response()->json($products, 200);
    }
    

    public function getProductByTime($time){ // lấy product dựa vào số ngày truyền vào
        try{
            $timeRs = Carbon::now()->subDays($time); // lấy ngày hiện tại và trừ đi số ngày truyền vào
            $products = Product::with('review','image','categorie')
            ->where('created_time' ,'>',  $timeRs)
            ->get();

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
            return response()->json($products,200);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);

        }
    }
    public function getProductsByHighestQuantity(){
        try{
            // Lấy các sản phẩm và sắp xếp theo số lượng giảm dần
            $products = Product::with('review', 'image', 'categorie')
                        ->orderBy('quantity', 'desc')
                        ->take(10) // Lấy ra 10 sản phẩm có số lượng lớn nhất
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
            return response()->json($products);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    
    public function getProductsByAverageRateAndSort(){
        try{
            // Lấy các sản phẩm và sắp xếp theo điểm đánh giá trung bình giảm dần
            $products = Product::with('review', 'image', 'categorie')
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
            $sortedProducts = $products->sortByDesc('average_rate')->values()->all();
            
            // Sắp xếp mảng sản phẩm theo averageRate giảm dần
            return response()->json($sortedProducts);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    
    public function getProductsByTopSaleAndSort(){
        try{
            // Lấy danh sách product_id đã được sắp xếp theo số lượng bán hàng
            $productIds = OrderDetail::select('product_id', DB::raw('COUNT(*) as count'))
                            ->groupBy('product_id')
                            ->orderByDesc('count')
                            ->pluck('product_id');
            
            // Lấy các sản phẩm chỉ từ danh sách product_id đã được sắp xếp
            $products = Product::with('review', 'image', 'categorie')
                        ->whereIn('id', $productIds)
                        ->orderByRaw("FIELD(id, " . implode(",", $productIds->toArray()) . ")")
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
    

    public function getProductsByPriceAndSort(){
        try{
            // Lấy các sản phẩm và sắp xếp theo số lượng giảm dần
            $products = Product::with('review', 'image', 'categorie')
                        ->orderBy('price', 'desc')
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
            return response()->json($products);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }

    public function getProductByName($name_product){ // lấy các product dựa vào tên
        try{
            $products = Product::with('review', 'image', 'categorie')
                        ->where('name', 'like', '%' . $name_product . '%')
                        ->get();
            
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
            return response()->json($products);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    public function getProductByNameId($name_product){ // lấy các product dựa vào tên
        try{
            $products = Product::with('review', 'image', 'categorie')
                        ->where('name', 'like', '%' . $name_product . '%')
                        ->orWhere('id', $name_product)
                        ->get();
            
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
            return response()->json($products);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }

    public function getProductByPrice($price){ // lấy các product dựa vào giá tiền
        try{
            $products = Product::with('review', 'image', 'categorie')
                        ->where('price', '<', $price)
                        ->get();

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
            return response()->json($products);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }

    public function getProductByCategory($category_id){ // lấy các product dựa vào id category
        try{
            $products = Product::with('review', 'image', 'categorie')
                        ->whereHas('categorie', function($query) use ($category_id){
                            $query->where('category.id', $category_id);
                        })->get();
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
            return response()->json($products);
        }catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }

    public function createProduct(Request $request){ //tạo product
        try{
            $product = new Product();
            $product->name = $request->name;
            $product->price = $request->price;
            $product->description = $request->description;
            $product->ingredient = $request->ingredient;
            $product->calo = $request->calo;
            $product->quantity = $request->quantity;
            $product->discount = $request->discount;
            $product->save();
            
            if ($request->hasFile('image')) {
                foreach ($request->file('image') as $image) {
                    // Lưu ảnh vào thư mục lưu trữ và lấy đường dẫn
                    $imagePath = $image->store('public/images');
                    
                    $imageName = basename($imagePath);
                    // Tạo bản ghi mới trong bảng Image
                    $product->image()->create(['imgurl' => $imageName]);
                }
            }

            if ($request->has('categories')) {
                $categories = json_decode($request->input('categories'), true);
                foreach ($categories as $category) {
                    $product->categorie()->attach($category);
                }
            }
            DB::commit();
            return response()->json(['message' => 'Product created successfully'], 201);
        }catch (\Exception $e){
            DB::rollBack();
            return response()->json(['message' => 'Failed to create product'.$e->getMessage()], 500);
        }
    }

    public function updateProduct($id,Request $request){ // cập nhật product
        $product = Product::find($id);
        if(!$product){
            return response()->json(['message'=> 'Product not found'],404);
        }
        try{
            $product->name = $request->name;
            $product->price = $request->price;
            $product->description = $request->description;
            $product->ingredient = $request->ingredient;
            $product->calo = $request->calo;
            $product->quantity = $request->quantity;
            $product->discount = $request->discount;
            $product->save();

            
            // if ($request->has('old_image')) {
            //     $oldImages = is_array($request->old_image) ? $request->old_image : json_decode($request->old_image, true);
                
            //     if (is_array($oldImages)) {
            //         $product->image()->delete();
                    
            //         foreach ($oldImages as $image) {
            //             $product->image()->create(['imgurl' => $image]);
            //         }
            //     }
            // }
            
            $product->image()->delete();

        // Thêm các ảnh từ old_image vào sản phẩm
        if ($request->has('old_image')) {
            $oldImages = is_array($request->old_image) ? $request->old_image : json_decode($request->old_image, true);
        
            if (is_array($oldImages)) {
                foreach ($oldImages as $image) {
                    $product->image()->create(['imgurl' => $image, 'product_id' => $product->id]);
                }
            }
        }

            // Xử lý ảnh mới
            if ($request->hasFile('image')) {
                foreach ($request->file('image') as $image) {
                    // Lưu ảnh vào thư mục lưu trữ và lấy đường dẫn
                    $imagePath = $image->store('public/images');
                    
                    $imageName = basename($imagePath);
                    // Tạo bản ghi mới trong bảng Image
                    $product->image()->create(['imgurl' => $imageName]);
                }
            }
            if ($request->has('categories')) {
                $categories = json_decode($request->input('categories'), true);
                $product->categorie()->sync($categories);
            }
            
            DB::commit();
            return response()->json(['message' => 'Product update successfully'], 201);
        }catch (\Exception $e){
            DB::rollBack();
            return response()->json(['message' => 'Failed to update product'.$e->getMessage()], 500);
        }
    }

    public function destroy($id){ // xóa product
        $product = Product::find($id);
        try{
            if(!$product){
                return response()->json(['message'=> 'Product not found'],404);
            }
            $product->delete();
            return response()->json(['message'=> 'Product delete successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to delete product'.$e->getMessage()],500);
        }
    }



    function getProductsByHotSearches()
{
    // Lấy danh sách top 5 từ khóa tìm kiếm
    $top5Searches = ProductController::getTop5HotSearches();

    $searchedProducts = [];
    $addedProductIds = []; // Mảng hash để lưu trữ ID của các sản phẩm đã thêm vào danh sách

    // Duyệt qua top 5 từ khóa
    foreach ($top5Searches as $keyword => $searchCount) {
        // Lấy danh sách sản phẩm dựa trên từ khóa
        $products = Product::with('review', 'image', 'categorie')
                            ->where('name', 'like', '%' . $keyword . '%')
                            ->get();

        // Số lượng sản phẩm đã thêm vào danh sách
        $addedProductsCount = 0;

        // Duyệt qua từng sản phẩm
        foreach ($products as $product) {
            // Kiểm tra xem ID của sản phẩm đã tồn tại trong danh sách chưa
            if (!in_array($product->id, $addedProductIds)) {
                // Tính tổng số điểm đánh giá của sản phẩm
                $totalRate = 0;
                $reviewCount = $product->review->count();
                foreach ($product->review as $review) {
                    $totalRate += $review->rate;
                }

                // Tính giá trị trung bình của đánh giá
                $averageRate = $reviewCount > 0 ? $totalRate / $reviewCount : 0;

                // Đếm số lần bán của sản phẩm
                $saleNumber = OrderDetail::where('product_id', $product->id)->count();

                // Thêm các thông tin tính toán vào đối tượng sản phẩm
                $product->average_rate = $averageRate;
                $product->sale_number = $saleNumber;

                // Thêm sản phẩm vào danh sách
                $searchedProducts[] = $product;
                $addedProductIds[] = $product->id; // Cập nhật mảng hash

                $addedProductsCount++;
            }

            // Kiểm tra xem đã đủ 5 sản phẩm hay chưa
            if ($addedProductsCount >= 5) {
                // Nếu đã đủ 5 sản phẩm, thoát khỏi vòng lặp
                break;
            }
        }
    }

    return $searchedProducts;
}


}
