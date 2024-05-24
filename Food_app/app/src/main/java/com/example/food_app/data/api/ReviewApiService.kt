package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Review.ReviewResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApiService {
    @GET("review/all/{product_id}")
    fun getReviewById(
        @Path("product_id") product_id :Int,
    ): Call<ReviewResponse>

    @FormUrlEncoded
    @POST("review/add")
    fun postReview(
        @Field("order_id") order_id: Int,
        @Field("product_id") product_id: Int,
        @Field("customer_id") customer_id: Int,
        @Field("rate") rate: Int,
        @Field("content") content: String?,
    ): Call<IsExistResponse>

    @FormUrlEncoded
    @POST("review")
    fun updateContent(
        @Field("id") id: Int,
        @Field("content") content: String?,
    ): Call<IsExistResponse>

    @DELETE("review")
    fun deleteReview(
        @Query("id") id :Int,
    ): Call<IsExistResponse>
}