package com.example.food_app.data.Repository

import com.example.food_app.data.api.OrderApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Orders.DeliveringOrderResponse
import com.example.food_app.data.model.Orders.OrderNotifiResponse
import com.example.food_app.data.model.Orders.OrdersResponse
import retrofit2.Call

class OrderRepository(
    private val orderApiService: OrderApiService
) {
    suspend fun createOrders(product_ids :List<Int>, customer_id : Int, note : String, payment : String,discount_id:Int,location_id:Int): Call<IsExistResponse> {
        return orderApiService.createOrders(product_ids, customer_id, note,payment,discount_id,location_id)
    }
    suspend fun createAOrders(product_id :Int,quantity : Int, customer_id : Int, note : String, payment : String,discount_id:Int,location_id:Int): Call<IsExistResponse> {
        return orderApiService.createAOrders(product_id,quantity, customer_id, note,payment,discount_id,location_id)
    }
    suspend fun getRatingProducts(customer_id : Int): Call<OrdersResponse> {
        return orderApiService.getRatingProducts(customer_id)
    }
    suspend fun getHistoryOrders(customer_id : Int): Call<OrdersResponse> {
        return orderApiService.getHistoryOrders(customer_id)
    }
    suspend fun getDeliveringProducts(customer_id : Int): Call<DeliveringOrderResponse> {
        return orderApiService.getDeliveringProducts(customer_id)
    }
    suspend fun deleteOrder(order_id : Int): Call<IsExistResponse> {
        return orderApiService.deleteOrder(order_id)
    }
    suspend fun getOrderInFiveDays(customer_id : Int): Call<OrderNotifiResponse> {
        return orderApiService.getOrderInFiveDays(customer_id)
    }
}