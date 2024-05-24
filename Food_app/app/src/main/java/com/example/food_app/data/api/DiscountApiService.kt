package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Discount.DiscountResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiscountApiService {
    @GET("discount/{discountCode}")
    fun isExistDiscount(
        @Path("discountCode") discountCode :String,
    ): Call<DiscountResponse>


    @GET("discount/new")
    fun getNewDiscount(
    ): Call<DiscountResponse>

    @FormUrlEncoded
    @POST("discount")
    fun checkUserUsed(
        @Field("discountCode") discountCode :String,
        @Field("customer_id") customer_id :Int
    ):Call<IsExistResponse>
}