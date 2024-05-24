package com.example.food_app.data.Repository

import com.example.food_app.data.api.ReviewApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import com.example.food_app.data.model.Review.ReviewResponse
import retrofit2.Call

class ReviewReponsitory(
    private val reviewApiService: ReviewApiService
) {
    suspend fun getReviewById(product_id :Int): Call<ReviewResponse> {
        return reviewApiService.getReviewById(product_id)
    }
    suspend fun postReview(order_id :Int,product_id :Int,customer_id :Int,rate :Int,content :String?): Call<IsExistResponse> {
        return reviewApiService.postReview(order_id,product_id,customer_id,rate,content)
    }
    suspend fun updateContent(id :Int,content :String?): Call<IsExistResponse> {
        return reviewApiService.updateContent(id,content)
    }
    suspend fun deleteReview(id :Int): Call<IsExistResponse> {
        return reviewApiService.deleteReview(id)
    }
}