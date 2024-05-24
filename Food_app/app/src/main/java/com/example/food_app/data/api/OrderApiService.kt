package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Orders.DeliveringOrderResponse
import com.example.food_app.data.model.Orders.OrderNotifiResponse
import com.example.food_app.data.model.Orders.OrdersResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {
    @FormUrlEncoded
    @POST("orders")
    fun createOrders(
        @Field("product_ids[]") product_ids: List<Int>,
        @Field("customer_id") customer_id: Int,
        @Field("note") note: String,
        @Field("payment") payment: String,
        @Field("discount_id") discount_id: Int,
        @Field("location_id") location_id: Int,
    ): Call<IsExistResponse>

    @FormUrlEncoded
    @POST("orders/single")
    fun createAOrders(
        @Field("product_id") product_id: Int,
        @Field("quantity") quantity: Int,
        @Field("customer_id") customer_id: Int,
        @Field("note") note: String,
        @Field("payment") payment: String,
        @Field("discount_id") discount_id: Int,
        @Field("location_id") location_id: Int
    ): Call<IsExistResponse>

    @GET("orders/getAllRating/{customer_id}/{status}")
    fun getRatingProducts(
        @Path("customer_id") customer_id: Int,
        @Path("status") status: String = "rating",
    ): Call<OrdersResponse>

    @GET("orders/getAllRating/{customer_id}/{status}")
    fun getHistoryOrders(
        @Path("customer_id") customer_id: Int,
        @Path("status") status: String = "completed",
    ): Call<OrdersResponse>

    @GET("orders/status/{customer_id}")
    fun getDeliveringProducts(
        @Path("customer_id") customer_id: Int,
    ): Call<DeliveringOrderResponse>

    @GET("orders/time/{customer_id}")
    fun getOrderInFiveDays(
        @Path("customer_id") customer_id: Int,
        ): Call<OrderNotifiResponse>

    @DELETE("orders/{order_id}")
    fun deleteOrder(
        @Path("order_id") order_id :Int,
    ): Call<IsExistResponse>

}