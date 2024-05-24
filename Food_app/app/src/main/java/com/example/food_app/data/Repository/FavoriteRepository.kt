package com.example.food_app.data.Repository

import com.example.food_app.data.api.AuthApiService
import com.example.food_app.data.api.FavoriteApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Product.ProductResponse
import retrofit2.Call

class FavoriteRepository(
    private val favoriteApiService: FavoriteApiService

) {
    suspend fun isExistProduct(customer_id:Int,product_id:Int): Call<IsExistResponse> {
        return favoriteApiService.isExistProduct(customer_id,product_id)
    }
    suspend fun deleteFavorite(customer_id:Int,product_id:Int): Call<IsExistResponse> {
        return favoriteApiService.deleteFavorite(customer_id,product_id)
    }
    suspend fun addFavorite(product_id:Int,customer_id:Int): Call<IsExistResponse> {
        return favoriteApiService.addFavorite(product_id,customer_id)
    }
    suspend fun getListFavorite(customer_id:Int): Call<ProductResponse> {
        return favoriteApiService.getListFavorite(customer_id)
    }
}