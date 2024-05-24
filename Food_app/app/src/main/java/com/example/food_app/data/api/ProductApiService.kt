package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Product.ProductResponse
import com.example.food_app.data.model.Product.ProductResponseItem
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("product")
    fun getAllProducts(
    ): Call<ProductResponse>

    @GET("product/{id}")
    fun getProductById(
        @Path("id") id :Int
    ): Call<ProductResponseItem>

    @GET("product/time/{time}")
    fun getAllProductsByTime(
        @Path("time") time :Int
    ): Call<ProductResponse>

    @GET("product/hotsearch")
    fun getAllProductsByHotSearch(
    ): Call<ProductResponse>

    @GET("product/recommend")
    fun getProductsRecommend(
    ): Call<ProductResponse>

    @GET("product/popular")
    fun getPopularProducts(
    ): Call<ProductResponse>

    @GET("product/name/{name}")
    fun getAllProductsByName(
        @Path("name") name :String
    ): Call<ProductResponse>

    @GET("product/priceSort")
    fun getProductsByPriceAndSort(
    ): Call<ProductResponse>

    @GET("product/rateSort")
    fun getProductsByRateAndSort(
    ): Call<ProductResponse>

    @GET("product/topSaleSort")
    fun getProductsByTopSaleAndSort(
    ): Call<ProductResponse>

    @GET("product/category/{category_id}")
    fun getProductByCategory(
        @Path("category_id") category_id :Int,
    ):Call<ProductResponse>
}