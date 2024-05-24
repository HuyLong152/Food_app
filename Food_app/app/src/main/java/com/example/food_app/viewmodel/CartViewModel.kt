package com.example.food_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.CartReponsitory
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Cart.CartResponse
import com.example.food_app.data.model.Cart.CartResponseItem
import com.example.food_app.data.model.Category.CategoryResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(
    private val cartRepository : CartReponsitory
): ViewModel() {
    val listAllCartLiveData : MutableLiveData<CartResponse> by lazy{
        MutableLiveData<CartResponse>()
    }

    fun getAllCart (
        customer_id : Int,
        onResult : (Boolean, CartResponse?) -> Unit
    ){
            viewModelScope.launch {
                try {
                    var response = cartRepository.getAllCart(customer_id)

                    response.enqueue(object : Callback<CartResponse> {
                        override fun onResponse(
                            call: Call<CartResponse>,
                            response: Response<CartResponse>
                        ) {
                            if (response.isSuccessful) {
                                val cartResponse = response.body()
                                if (cartResponse != null) {
                                    listAllCartLiveData.value = cartResponse
                                    onResult(true, cartResponse)
                                }
                            } else {
                                onResult(false, null)
                            }
                        }

                        override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                            onResult(false, null)
                        }

                    })
                } catch (e: Exception) {
                    onResult(false, null)
                }
            }
    }

    fun updateQuantity (
        customer_id : Int,
        product_id :Int,
        quantity :Int,
        onResult : (Boolean, String?) -> Unit
    ){
            viewModelScope.launch {
                try {
                    var response = cartRepository.updateQuantity(customer_id, product_id,quantity)

                    response.enqueue(object : Callback<IsExistResponse> {
                        override fun onResponse(
                            call: Call<IsExistResponse>,
                            response: Response<IsExistResponse>
                        ) {
                            if (response.isSuccessful) {
                                val cartResponse = response.body()
                                if (cartResponse!!.message == "Update cart successful") {
                                    onResult(true, "Update cart successful")
                                }
                            } else {
                                onResult(false, null)
                            }
                        }

                        override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                            onResult(false, null)
                        }

                    })
                } catch (e: Exception) {
                    onResult(false, null)
                }
        }
    }

    fun getAProductCart (
        customer_id : Int,
        product_id :Int,
        onResult : (Boolean, CartResponseItem?) -> Unit
    ){
        viewModelScope.launch {
            try {
                var response = cartRepository.getAProductCart(customer_id, product_id)

                response.enqueue(object : Callback<CartResponseItem> {
                    override fun onResponse(
                        call: Call<CartResponseItem>,
                        response: Response<CartResponseItem>
                    ) {
                        if (response.isSuccessful) {
                            val cartResponse = response.body()
                            if (cartResponse != null) {
                                onResult(true, cartResponse)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<CartResponseItem>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }

    fun deleteCart (
        customer_id : Int,
        product_id :Int,
        onResult : (Boolean, String?) -> Unit
    ){
        viewModelScope.launch {
            try {
                var response = cartRepository.deleteCart(customer_id, product_id)

                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val cartResponse = response.body()
                            if (cartResponse!!.message == "Product delete successful") {
                                onResult(true, "Product delete successful")
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }

    fun addToCart (
        customer_id : Int,
        product_id :Int,
        quantity :Int,
        onResult : (Boolean, String?) -> Unit
    ){
        viewModelScope.launch {
            try {
                var response = cartRepository.addToCart(customer_id, product_id,quantity)

                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val cartResponse = response.body()
                            if (cartResponse!!.message == "Add product to cart successfully") {
                                onResult(true, "Add product to cart successfully")
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }
    fun isExistInCart (
        customer_id : Int,
        product_id :Int,
        onResult : (Boolean, String?) -> Unit
    ){
        viewModelScope.launch {
            try {
                var response = cartRepository.isExistInCart(customer_id, product_id)

                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val cartResponse = response.body()
                            if (cartResponse!!.message == "Product already exists") {
                                onResult(true, "Product already exists")
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }

    fun getProductsCart (
        customer_id : Int,
        product_ids :List<Int>,
        onResult : (Boolean, CartResponse?) -> Unit
    ){
        viewModelScope.launch {
            try {
                var response = cartRepository.getProductsCart(customer_id, product_ids)

                response.enqueue(object : Callback<CartResponse> {
                    override fun onResponse(
                        call: Call<CartResponse>,
                        response: Response<CartResponse>
                    ) {
                        if (response.isSuccessful) {
                            val cartResponse = response.body()
                            if (cartResponse != null) {
                                onResult(true, cartResponse)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }
}