package com.example.food_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.Repository.FavoriteRepository
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Product.ProductResponse
import com.example.food_app.data.model.Product.ProductResponseItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel(
    private val favoriteRepository : FavoriteRepository
):ViewModel() {
    val listAllFavoriteLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }

    fun isFavorite(product_id: Int): Boolean {
//        Log.d("abc",listAllFavoriteLiveData.value!!.size.toString())
//        Log.d("abc",product_id.toString())
//        val favoriteList = listAllFavoriteLiveData.value ?: return false
        if(listAllFavoriteLiveData.value != null){
            return listAllFavoriteLiveData.value!!.any { it.id == product_id }
        }
        return false
    }

    fun getAllFavorite(
        customer_id: Int,
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
            viewModelScope.launch {
                try {
                    var response = favoriteRepository.getListFavorite(customer_id)

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
                                    listAllFavoriteLiveData.value = productResponse
                                    onResult(true, productResponse)
                                }
                            } else {
                                onResult(false, null)
                            }
                        }

                        override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                            onResult(false, null)
                        }

                    })
                } catch (e: Exception) {
                    onResult(false, null)
                }
            }
    }

    fun isExistProduct(
        customer_id :Int,
        product_id: Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = favoriteRepository.isExistProduct(customer_id,product_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productResponse = response.body()
                            if (productResponse!!.message == "Product already exists") {
                                onResult(true, "Product already exists")
                            }
                        } else {
                            onResult(false, "Product does not exist")
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
    fun deleteFavorite(
        customer_id :Int,
        product_id: Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = favoriteRepository.deleteFavorite(customer_id,product_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productResponse = response.body()
                            if (productResponse!!.message == "Product delete successful") {
                                onResult(true, "Product delete successful")
                            }
                        } else {
                            onResult(false, "Failed to delete product")
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

    fun addFavorite(
        product_id: Int,
        customer_id :Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = favoriteRepository.addFavorite(product_id, customer_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productResponse = response.body()
                            if (productResponse!!.message == "Add product to favorite successfully") {
                                onResult(true, "Add product to favorite successfully")
                            }
                        } else {
                            onResult(false, "Add product to favorite fail")
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