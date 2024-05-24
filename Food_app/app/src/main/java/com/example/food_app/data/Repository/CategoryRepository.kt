package com.example.food_app.data.Repository

import com.example.food_app.data.api.AuthApiService
import com.example.food_app.data.api.CategoryApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import com.example.food_app.data.model.Category.CategoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class CategoryRepository(
    private val categoryApiService: CategoryApiService
) {
    suspend fun getAllCategories():Call<CategoryResponse>{
        return categoryApiService.getAllCategories()
    }
}