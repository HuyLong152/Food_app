package com.example.food_app.data.Repository

import com.example.food_app.data.api.AuthApiService
import com.example.food_app.data.api.CartApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Cart.CartResponse
import com.example.food_app.data.model.Cart.CartResponseItem
import retrofit2.Call

class CartReponsitory(
    private val cartApiService: CartApiService
) {
    suspend fun getAllCart(customer_id:Int): Call<CartResponse> {
        return cartApiService.getAllCart(customer_id)
    }
    suspend fun updateQuantity(customer_id:Int,product_id:Int,quantity:Int): Call<IsExistResponse> {
        return cartApiService.updateQuantity(customer_id, product_id,quantity)
    }
    suspend fun getAProductCart(customer_id:Int,product_id:Int): Call<CartResponseItem> {
        return cartApiService.getAProductCart(customer_id, product_id)
    }
    suspend fun deleteCart(customer_id:Int,product_id:Int): Call<IsExistResponse> {
        return cartApiService.deleteCart(customer_id, product_id)
    }
    suspend fun addToCart(customer_id:Int,product_id:Int,quantity:Int): Call<IsExistResponse> {
        return cartApiService.addToCart(customer_id, product_id,quantity)
    }
    suspend fun isExistInCart(customer_id:Int,product_id:Int): Call<IsExistResponse> {
        return cartApiService.isExistInCart(customer_id, product_id)
    }
    suspend fun getProductsCart(customer_id:Int,product_ids:List<Int>): Call<CartResponse> {
        return cartApiService.getProductsCart(customer_id, product_ids)
    }
}
