<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Discount;
use App\Models\Customer_discount;


class DiscountController extends Controller
{
    //

    public function createDiscount(Request $request)
{
    try {
        // Kiểm tra xem discount_code đã tồn tại chưa
        $existingDiscount = Discount::where('discount_code', $request->discount_code)->first();

        if ($existingDiscount) {
            // Nếu discount_code đã tồn tại, trả về thông báo lỗi
            return response()->json(['message' => 'Discount code already exists'], 200);
        }

        // Nếu discount_code chưa tồn tại, tiến hành thêm mới
        $discount = new Discount;
        $discount->discount_code = $request->discount_code;
        $discount->discount_percent = $request->discount_percent;
        $discount->expiration_time = $request->has('expiration_time') ? $request->expiration_time : null;
        $discount->available = $request->available;
        $discount->number_uses = $request->has('number_uses') ? $request->number_uses : null;
        $discount->save();

        return response()->json(['message' => 'Discount create successful'], 200);
    } catch(\Exception $e) {
        // Xử lý ngoại lệ
        return response()->json(['message' => 'Discount create fail'], 500);
    }
}

    public function updateDiscount(Request $request)
    {
        $discount = Discount::find($request->id);
        
        if (!$discount) {
            return response()->json(['message' => 'No discount code exists with the given ID'], 404);
        }
        
        $discount->discount_code = $request->discount_code;
        $discount->discount_percent = $request->discount_percent;
        $discount->expiration_time = $request->has('expiration_time') ? $request->expiration_time : null;
        $discount->available = $request->available;
        $discount->number_uses = $request->has('number_uses') ? $request->number_uses : null;
        $discount->save();
    
        return response()->json(['message' => 'Discount update successful'], 200);
    }
    
    public function deleteDiscount($id)
    {
        $discount = Discount::find($id);
        
        if (!$discount) {
            return response()->json(['message' => 'No discount code exists with the given ID'], 404);
        }
        Customer_discount::where('discount_id', $id)->delete();
        $discount->delete();
        return response()->json(['message' => 'Discount delete successful'], 200);
    }

    public function getADiscount($id)
    {
        $discount = Discount::find($id);
    
        if ($discount) {
            return response()->json($discount, 200);
        } else {
            return response()->json(['message' => 'No discount code exists with the given ID'], 404);
        }
    }
    
    public function getDiscountBySearch($textSearch)
{
    $discounts = Discount::where('id', 'like', "%$textSearch%")
                        ->orWhere('discount_code', 'like', "%$textSearch%")
                        ->orWhere('discount_percent', 'like', "%$textSearch%")
                        ->get();

    if ($discounts->isNotEmpty()) {
        return response()->json($discounts, 200);
    } else {
        return response()->json(['message' => 'No discount code exists with the given criteria'], 404);
    }
}


    public function getAllDiscount()
    {
        try{
            $latestDiscount = Discount::get();
                                    return response()->json($latestDiscount, 200);
        }catch(\Exception $e){
            return response()->json(['message' => 'No discount code available'], 404);
        }
    }
    public function getLatestDiscountCode()
{
    try {
        $latestDiscount = Discount::where('available', 1)
            ->where(function ($query) {
                $query->where('number_uses', '>', 1) // Số lần sử dụng > 1
                    ->orWhereNull('number_uses'); // hoặc số lần sử dụng là null
            })
            ->where(function ($query) {
                $query->whereNull('expiration_time') // Thời gian hết hạn là null
                    ->orWhere('expiration_time', '>', now()); // hoặc thời gian hết hạn lớn hơn thời điểm hiện tại
            })
            ->orderBy('create_time', 'desc')
            ->first();

        if ($latestDiscount) {
            return response()->json($latestDiscount, 200);
        } else {
            return response()->json(['message' => 'No discount code available'], 404);
        }
    } catch (\Exception $e) {
        return response()->json(['message' => 'No discount code available'], 404);
    }
}

    public function checkDiscountCodeExists($discountCode)
{
    // Kiểm tra xem mã giảm giá đã tồn tại hay không
    $discount = Discount::where('discount_code', $discountCode)
                        ->where('available', 1)
                        ->first();
    if($discount){
        return response()->json($discount, 200);
    }else{
        return response()->json(['message' => 'Discount code does not exist'], 400);
    }
}

public function isUsedDiscountCode(Request $request)
{
    $discount = Discount::where('discount_code', $request->discountCode)->first();

    if ($discount) {
        // Kiểm tra số lượt sử dụng
        if ($discount->number_uses !== null && $discount->number_uses < 1) {
            return response()->json(['message' => 'Discount code has reached its usage limit'], 200);
        }

        if ($discount->expiration_time === null) {
            $customerDiscount = Customer_discount::where('customer_id', $request->customer_id)
                                                ->where('discount_id', $discount->id)
                                                ->first();

            if ($customerDiscount) {
                return response()->json(['message' => 'Discount code already used by this customer'], 200);
            } else {
                return response()->json(['message' => 'Discount code is available for this customer'], 200);
            }
        } else {
            $currentTime = now(); // Thời gian hiện tại
            $expirationTime = $discount->expiration_time;

            if ($currentTime > $expirationTime) {
                return response()->json(['message' => 'Discount code has expired'], 200);
            }
            $customerDiscount = Customer_discount::where('customer_id', $request->customer_id)
                                                ->where('discount_id', $discount->id)
                                                ->first();
            if ($customerDiscount) {
                return response()->json(['message' => 'Discount code already used by this customer'], 200);
            } else {
                return response()->json(['message' => 'Discount code is available for this customer'], 200);
            }
        }
    } else {
        return response()->json(['message' => 'Discount code does not exist'], 200);
    }
}



    
}
