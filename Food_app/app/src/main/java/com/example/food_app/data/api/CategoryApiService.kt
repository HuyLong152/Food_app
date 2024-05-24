package com.example.food_app.data.api

import com.example.food_app.data.model.Category.CategoryResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApiService {
    @GET("category")
    fun getAllCategories(
    ): Call<CategoryResponse>

}