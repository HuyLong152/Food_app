package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import com.example.food_app.data.model.Cart.CartResponse
import com.example.food_app.data.model.Cart.CartResponseItem
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CartApiService {
    @GET("cart/{customer_id}")
    fun getAllCart(
        @Path("customer_id") customer_id :Int,
    ): Call<CartResponse>

    @GET("cart/product/{customer_id}/{product_id}")
    fun getAProductCart(
        @Path("customer_id") customer_id :Int,
        @Path("product_id") product_id :Int,
    ): Call<CartResponseItem>

    @FormUrlEncoded
    @POST("cart")
    fun updateQuantity(
        @Field("customer_id") customer_id :Int,
        @Field("product_id") product_id :Int,
        @Field("quantity") quantity :Int,
    ):Call<IsExistResponse>

    @DELETE("cart")
    fun deleteCart(
        @Query("customer_id") customer_id :Int,
        @Query("product_id") product_id :Int,
    ): Call<IsExistResponse>

    @FormUrlEncoded
    @POST("cart/add")
    fun addToCart(
        @Field("customer_id") customer_id :Int,
        @Field("product_id") product_id :Int,
        @Field("quantity") quantity :Int,
    ):Call<IsExistResponse>

    @GET("cart/{customer_id}/{product_id}")
    fun isExistInCart(
        @Path("customer_id") customer_id :Int,
        @Path("product_id") product_id :Int,
    ): Call<IsExistResponse>

    @GET("cart/payment")
    fun getProductsCart(
        @Query("customer_id") customer_id: Int,
        @Query("product_ids[]") product_ids: List<Int>
    ): Call<CartResponse>
}