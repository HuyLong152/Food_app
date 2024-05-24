package com.example.food_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.OrderRepository
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Orders.DeliveringOrderResponse
import com.example.food_app.data.model.Orders.OrderNotifiResponse
import com.example.food_app.data.model.Orders.OrdersResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    val listDeliveringOrdersLiveData: MutableLiveData<DeliveringOrderResponse> by lazy {
        MutableLiveData<DeliveringOrderResponse>()
    }

    fun createOrders(
        product_ids: List<Int>,
        customer_id: Int,
        note: String,
        payment: String,
        discount_id :Int,
        location_id :Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = orderRepository.createOrders(product_ids, customer_id, note, payment,discount_id,location_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Add new order successfully") {
                                onResult(true, isExistResponse.message)
                            }
                        } else {
                            onResult(false, response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false, "add new order fail")
                    }

                })
            } catch (e: Exception) {
                onResult(false, e.message!!)
            }
        }
    }


    fun getOrderInFiveDays(
        customer_id: Int,
        onResult: (Boolean, OrderNotifiResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = orderRepository.getOrderInFiveDays(customer_id)
                response.enqueue(object : Callback<OrderNotifiResponse> {
                    override fun onResponse(
                        call: Call<OrderNotifiResponse>,
                        response: Response<OrderNotifiResponse>
                    ) {
                        if (response.isSuccessful) {
                            val orderResponse = response.body()
                            if (orderResponse != null) {
                                onResult(true, orderResponse)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<OrderNotifiResponse>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }

    fun createAOrders(
        product_id: Int,
        quantity: Int,
        customer_id: Int,
        note: String,
        payment: String,
        discount_id :Int,
        location_id :Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response =
                    orderRepository.createAOrders(product_id, quantity, customer_id, note, payment,discount_id,location_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Add new order successfully") {
                                onResult(true, isExistResponse.message)
                            }
                        } else {
                            onResult(false, response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false, "add new order fail")
                    }

                })
            } catch (e: Exception) {
                onResult(false, e.message!!)
            }
        }
    }

    fun getRatingProducts(
        customer_id: Int,
        onResult: (Boolean, OrdersResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = orderRepository.getRatingProducts(customer_id)
                response.enqueue(object : Callback<OrdersResponse> {
                    override fun onResponse(
                        call: Call<OrdersResponse>,
                        response: Response<OrdersResponse>
                    ) {
                        if (response.isSuccessful) {
                            val orderResponse = response.body()
                            if (orderResponse != null) {
                                onResult(true, orderResponse)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }

    fun getHistoryOrders(
        customer_id: Int,
        onResult: (Boolean, OrdersResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = orderRepository.getHistoryOrders(customer_id)
                response.enqueue(object : Callback<OrdersResponse> {
                    override fun onResponse(
                        call: Call<OrdersResponse>,
                        response: Response<OrdersResponse>
                    ) {
                        if (response.isSuccessful) {
                            val orderResponse = response.body()
                            if (orderResponse != null) {
                                onResult(true, orderResponse)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }

                    override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                        onResult(false, null)
                    }

                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }

    fun getDeliveringProducts(
        customer_id: Int,
        onResult: (Boolean, DeliveringOrderResponse?) -> Unit
    ) {
            viewModelScope.launch {
                try {
                    var response = orderRepository.getDeliveringProducts(customer_id)
                    response.enqueue(object : Callback<DeliveringOrderResponse> {
                        override fun onResponse(
                            call: Call<DeliveringOrderResponse>,
                            response: Response<DeliveringOrderResponse>
                        ) {
                            if (response.isSuccessful) {
                                val orderResponse = response.body()
                                if (orderResponse != null) {
                                    onResult(true, orderResponse)
                                }
                            } else {
                                Log.d("abc","sai")
                                onResult(false, null)
                            }
                        }

                        override fun onFailure(call: Call<DeliveringOrderResponse>, t: Throwable) {
                            onResult(false, null)
                            Log.d("abc","sai1")

                        }

                    })
                } catch (e: Exception) {
                    onResult(false, null)
                    Log.d("abc","sai2")

                }
            }
    }

    fun deleteOrder(
        order_id: Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = orderRepository.deleteOrder(order_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val orderResponse = response.body()
                            if (orderResponse!!.message == "Order deleted successfully") {
                                onResult(true, "Order deleted successfully")
                            }
                        } else {
                            onResult(false, "Delete order failed")
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
}