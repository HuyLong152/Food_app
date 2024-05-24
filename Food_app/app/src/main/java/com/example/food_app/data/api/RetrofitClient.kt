package com.example.food_app.data.api

import com.example.food_app.BASE_URL_API
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit :Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val authApiService : AuthApiService by lazy{
        retrofit.create(AuthApiService::class.java)
    }
    val customerApiService : CustomerApiService by lazy{
        retrofit.create(CustomerApiService::class.java)
    }
    val locationApiService : LocationApiService by lazy{
        retrofit.create(LocationApiService::class.java)
    }
    val categoryApiService : CategoryApiService by lazy{
        retrofit.create(CategoryApiService::class.java)
    }
    val productApiService : ProductApiService by lazy{
        retrofit.create(ProductApiService::class.java)
    }
    val favoriteApiService : FavoriteApiService by lazy{
        retrofit.create(FavoriteApiService::class.java)
    }
    val cartApiService : CartApiService by lazy{
        retrofit.create(CartApiService::class.java)
    }
    val orderApiService : OrderApiService by lazy{
        retrofit.create(OrderApiService::class.java)
    }
    val reviewApiService : ReviewApiService by lazy{
        retrofit.create(ReviewApiService::class.java)
    }
    val discountApiService : DiscountApiService by lazy{
        retrofit.create(DiscountApiService::class.java)
    }
    val hotSearchApiService : HotSearchApiService by lazy{
        retrofit.create(HotSearchApiService::class.java)
    }
}