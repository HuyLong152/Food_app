package com.example.food_app.data.Repository

import com.example.food_app.data.api.DiscountApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Discount.DiscountResponse
import retrofit2.Call

class DiscountRepository(
    private val discountApiService: DiscountApiService
) {
    suspend fun isExistDiscount(discountCode:String): Call<DiscountResponse> {
        return discountApiService.isExistDiscount(discountCode)
    }
    suspend fun getNewDiscount(): Call<DiscountResponse> {
        return discountApiService.getNewDiscount()
    }
    suspend fun checkUserUsed(discountCode:String, customer_id:Int): Call<IsExistResponse> {
        return discountApiService.checkUserUsed(discountCode,customer_id)
    }
}