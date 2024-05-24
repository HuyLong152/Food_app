package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Product.ProductResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoriteApiService {
    @GET("favorite/{customer_id}/{product_id}")
    fun isExistProduct(
        @Path("customer_id") customer_id :Int,
        @Path("product_id") product_id :Int,
    ): Call<IsExistResponse>

    @DELETE("favorite")
    fun deleteFavorite(
        @Query("customer_id") customer_id :Int,
        @Query("product_id") product_id :Int,
    ): Call<IsExistResponse>

    @FormUrlEncoded
    @POST("favorite/add")
    fun addFavorite(
        @Field("product_id") product_id :Int,
        @Field("customer_id") customer_id :Int,
    ): Call<IsExistResponse>

    @GET("favorite/{customer_id}")
    fun getListFavorite(
        @Path("customer_id") customer_id :Int,
    ): Call<ProductResponse>
}