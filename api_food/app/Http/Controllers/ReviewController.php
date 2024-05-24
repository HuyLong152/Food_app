<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Review;
use App\Models\Orders;


class ReviewController extends Controller
{
    //
    public function index($customer_id){
        try {
            // Lấy thông tin đơn hàng và các sản phẩm của đơn hàng đó dựa vào customer_id và status
            $reviews = Review::where('customer_id', $customer_id)->get();

            // Trả về dữ liệu đơn hàng và các sản phẩm tương ứng dưới dạng JSON
            return response()->json($reviews, 200);
        } catch (\Exception $e) {
            // Nếu có lỗi xảy ra, trả về thông báo lỗi
            return response()->json(["error" => $e->getMessage()]);
        }
    }
    public function getAllbById($product_id){
        // try {
        //     // Lấy thông tin đơn hàng và các sản phẩm của đơn hàng đó dựa vào customer_id và status
        //     $reviews = Review::where('product_id', $product_id)->get();

        //     // Trả về dữ liệu đơn hàng và các sản phẩm tương ứng dưới dạng JSON
        //     return response()->json($reviews, 200);
        // } catch (\Exception $e) {
        //     // Nếu có lỗi xảy ra, trả về thông báo lỗi
        //     return response()->json(["error" => $e->getMessage()]);
        // }
        try {
            // Lấy danh sách các đánh giá của sản phẩm với thông tin khách hàng
            $reviews = Review::where('product_id', $product_id)
                ->join('customer', 'review.customer_id', '=', 'customer.id')
                ->select('review.*', 'customer.full_name', 'customer.phone_number', 'customer.email', 'customer.image_url', 'customer.social_link')
                ->orderBy('review.updated_time', 'desc')
                ->get();
    
            // Trả về dữ liệu đánh giá và thông tin khách hàng tương ứng dưới dạng JSON
            return response()->json($reviews, 200);
        } catch (\Exception $e) {
            // Nếu có lỗi xảy ra, trả về thông báo lỗi
            return response()->json(["error" => $e->getMessage()]);
        }
    }
    public function addNewReview(Request $request){
        try{
            $review = new Review();
            $review->product_id = $request -> product_id;
            $review->customer_id = $request -> customer_id;
            $review->rate = $request -> rate;
            $review->content =$request->content;
            $review->save();

            $order = Orders::find($request->order_id);
            $order->status = "completed";
            $order->save();
            return response()->json(['message' => 'Add review successfully'], 200);
        }catch (\Exception $e){
            return response()->json(["message"=> "Add review faild "], 400);
        }
    }
    public function updateNewReview(Request $request){
        // try{
        //     $review = Review::where("customer_id",$request->customer_id) // sửa dựa vào customer_id và product_id
        //                         -> where("product_id",$request->product_id)
        //                         ->first();
        //     if($review){
        //         $review->rate = $request -> rate;
        //         $review->content = $request -> content;
        //         $review->save();
        //         return response()->json(['message' => 'update review successfully'], 200);
        //     }else{
        //         return response()->json(['message'=> 'review not found'],400);
        //     }
        // }catch (\Exception $e){
        //     return response()->json(["message"=> "update review faild "], 500);
        // }

        try{
            $review = Review::find($request->id); // sửa dựa vào id
            if($review){
                // $review->rate = $request -> rate;
                $review->content = $request -> content;
                $review->save();
                return response()->json(['message' => 'update review successfully'], 200);
            }else{
                return response()->json(['message'=> 'review not found'],400);
            }
        }catch (\Exception $e){
            return response()->json(["message"=> "update review fail"], 500);
        }
    }
    public function destroy(Request $request){
        // try{
        //     $review = Review::where('customer_id',$request->customer_id) // xóa dựa vào customer_id và product_id
        //                 ->where('product_id', $request->product_id)
        //                 ->first();
        //     if(!$review){
        //         return response()->json(['message'=> 'Review not found'],404);
        //     }
        //     $review->delete();
        //     return response()->json(['message'=> 'Review delete successful'],200);
        // }
        // catch(\Throwable $e){
        //     return response()->json(['message'=> 'Failed to delete review'],500);
        // }

        try{
            $review = Review::find($request -> id); // xóa dựa vào id
            if(!$review){
                return response()->json(['message'=> 'Review not found'],404);
            }
            $review->delete();
            return response()->json(['message'=> 'Review delete successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to delete review'],500);
        }
    }
}
