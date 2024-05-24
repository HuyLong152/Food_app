<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Cart;
use App\Models\Product;
use App\Models\OrderDetail;
use App\Models\Image;

class CartController extends Controller
{
    //
    public function index($customer_id,$product_id){
        $cart = Cart::where('customer_id', $customer_id)
            ->where('product_id', $product_id)
            ->first();
        if ($cart) {
            return response()->json(['message' => 'Product already exists'], 200);
        } else {
            return response()->json(['message' => 'Product does not exist'], 404);
        }
    }
    public function getProductInCart(Request $request){
        $cart = Cart::where('customer_id', $request->customer_id)
            ->where('product_id', $request -> product_id)
            ->first();
        if ($cart) {
            return response()->json(['message' => $cart], 200);
        } else {
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    public function getByCustomer($customer_id){
        try{
            // Lấy danh sách sản phẩm trong giỏ hàng của khách hàng
            $cartProducts = Cart::where('customer_id', $customer_id)
                                ->select('product_id', 'quantity')
                                ->get();
    
            // Lấy danh sách sản phẩm từ bảng Product có trong giỏ hàng
            $products = Product::with('review', 'image', 'categorie')
                        ->whereIn('id', $cartProducts->pluck('product_id'))
                        ->get();
            
            // Tính điểm đánh giá trung bình và số lượng sản phẩm đã bán cho từng sản phẩm
            foreach ($products as $product) {
                $totalRate = 0;
                $reviewCount = $product->review->count();
                foreach ($product->review as $review) {
                    $totalRate += $review->rate;
                }
                $product->average_rate = ($reviewCount > 0) ? ($totalRate / $reviewCount) : 0;
                
                $productId = $product->id;
                $saleNumber = OrderDetail::where('product_id', $productId)->count();
                $product->sale_number = $saleNumber;
                
                // Lấy số lượng sản phẩm trong giỏ hàng tương ứng
                $cartQuantity = $cartProducts->where('product_id', $productId)->first()->quantity;
                $product->cart_quantity = $cartQuantity;
            }
            
            return response()->json($products);
        } catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    
    public function getAProductByCustomer($customer_id, $product_id) {
        try {
            // Lấy sản phẩm trong giỏ hàng của khách hàng
            $cartProduct = Cart::where('customer_id', $customer_id)
                               ->where('product_id', $product_id)
                               ->select('product_id', 'quantity')
                               ->first();
    
            if (!$cartProduct) {
                return response()->json(['message' => 'Product not found in cart'], 404);
            }
    
            // Lấy thông tin sản phẩm
            $product = Product::with('review', 'image', 'categorie')
                              ->find($product_id);
    
            if (!$product) {
                return response()->json(['message' => 'Product not found'], 404);
            }
    
            // Tính điểm đánh giá trung bình và số lượng sản phẩm đã bán
            $totalRate = 0;
            $reviewCount = $product->review->count();
            foreach ($product->review as $review) {
                $totalRate += $review->rate;
            }
            $product->average_rate = ($reviewCount > 0) ? ($totalRate / $reviewCount) : 0;
    
            $saleNumber = OrderDetail::where('product_id', $product_id)->count();
            $product->sale_number = $saleNumber;
    
            // Số lượng sản phẩm trong giỏ hàng
            $product->cart_quantity = $cartProduct->quantity;
    
            return response()->json($product);
        } catch (\Exception $e) {
            return response()->json(['message' => 'Internal Server Error'], 500);
        }
    }
    


    public function getProductsInfoByCustomer(Request $request) {
        try {
            // Khởi tạo một mảng để lưu thông tin sản phẩm
            $product_ids = $request->product_ids;
            $customer_id = $request->customer_id;
            $productsInfo = [];
    
            // Duyệt qua danh sách product_ids
            foreach ($product_ids as $product_id) {
                // Lấy thông tin sản phẩm cho từng product_id
                $productInfo = $this->getAProductByCustomer($customer_id, $product_id);
    
                // Kiểm tra nếu có lỗi xảy ra khi lấy thông tin sản phẩm
                if ($productInfo->getStatusCode() != 200) {
                    // Trả về lỗi cho người dùng
                    return response()->json(['message' => 'Error getting product information'], $productInfo->getStatusCode());
                }
    
                // Thêm thông tin sản phẩm vào mảng productsInfo
                $productsInfo[] = $productInfo->getData();
            }
    
            // Trả về mảng chứa thông tin các sản phẩm
            return response()->json($productsInfo);
        } catch (\Exception $e) {
            // Trả về lỗi nếu có lỗi xảy ra trong quá trình xử lý
            return response()->json(['message' => 'Internal Server Error'], 500);
        }
    }
    
    

    public function updateAProduct(Request $request){
        $cart = Cart::where('customer_id', $request->customer_id)
        ->where('product_id', $request->product_id)
        ->first();
        if (!$cart) {
            return response()->json(['message' => 'Product not found'], 404);
        }else{
            $cart->quantity = $request->quantity;
            $cart->save();
            return response()->json(['message' => "Update cart successful"], 200);
        }
        
    }

    public function addNewProduct(Request $request){
        try{
            $cart = new Cart();
            $cart->product_id = $request -> product_id;
            $cart->customer_id = $request -> customer_id;
            $cart->quantity = $request -> quantity;
            $cart->save();
            return response()->json(['message' => 'Add product to cart successfully'], 200);
        }catch (\Exception $e){
            return response()->json(["message"=> "Add product to cart faild ".$e->getMessage()], 400);
        }
    }

    public function destroy(Request $request){
        try{
            $cart = Cart::where('customer_id',$request->customer_id)
                        ->where('product_id', $request->product_id)
                        ->first();
            if(!$cart){
                return response()->json(['message'=> 'Product not found'],404);
            }
            $cart->delete();
            return response()->json(['message'=> 'Product delete successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to delete product'.$e->getMessage()],500);
        }
    }
}
