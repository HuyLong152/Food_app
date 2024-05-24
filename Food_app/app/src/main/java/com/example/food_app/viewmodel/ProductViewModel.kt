package com.example.food_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.ProductRepository
import com.example.food_app.data.model.Product.ProductResponse
import com.example.food_app.data.model.Product.ProductResponseItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    val listAllProductLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductByTimeLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductByNameLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductPopularLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductPriceSortLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductPriceRateLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductTopSaleLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductByCategoryLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductByHotSearchLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }
    val listAllProductRecommendLiveData: MutableLiveData<ProductResponse> by lazy {
        MutableLiveData<ProductResponse>()
    }

    fun getProductsRecommend(
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductRecommendLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getProductsRecommend()

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
                                    onResult(true, productResponse)
                                    listAllProductRecommendLiveData.value = productResponse
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
//        }
    }


    fun getAllProductsByHotSearch(
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductByHotSearchLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getAllProductsByHotSearch()

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
                                    onResult(true, productResponse)
                                    listAllProductByHotSearchLiveData.value = productResponse
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
//        }
    }

    fun getAllProducts(
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getAllProduct()

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
                                    onResult(true, productResponse)
                                    listAllProductLiveData.value = productResponse
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
//        }
    }


    fun getAllProductsByTime(
        time: Int,
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductByTimeLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getAllProductsByTime(time)

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
                                    listAllProductByTimeLiveData.value = productResponse
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
//        }

    }

    fun getAllProductsByName(
        name: String,
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductByNameLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getAllProductsByName(name)

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
//                                    listAllProductByNameLiveData.value = productResponse
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
//        }

    }

    fun getPopularProducts(
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductPopularLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getPopularProducts()

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
                                    listAllProductPopularLiveData.value = productResponse
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
//        }

    }

    fun getProductsByPriceAndSort(
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductPriceSortLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getProductsByPriceAndSort()

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
//                                    listAllProductPriceSortLiveData.value = productResponse
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
//        }

    }

    fun getProductsByRateAndSort(
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductPriceRateLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getProductsByRateAndSort()

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
//                                    listAllProductPriceRateLiveData.value = productResponse
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
//        }

    }

    fun getProductsByTopSaleAndSort(
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductTopSaleLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getProductsByTopSaleAndSort()

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
//                                    listAllProductTopSaleLiveData.value = productResponse
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
//        }

    }

    fun getAllProductsByCategory(
        category_id: Int,
        onResult: (Boolean, ProductResponse?) -> Unit
    ) {
//        val cachedProducts = listAllProductByCategoryLiveData.value
//        if (cachedProducts != null) {
//            onResult(true, cachedProducts)
//        } else {
            viewModelScope.launch {
                try {
                    var response = productRepository.getProductsByCategory(category_id)

                    response.enqueue(object : Callback<ProductResponse> {
                        override fun onResponse(
                            call: Call<ProductResponse>,
                            response: Response<ProductResponse>
                        ) {
                            if (response.isSuccessful) {
                                val productResponse = response.body()
                                if (productResponse != null) {
//                                    listAllProductByCategoryLiveData.value = productResponse
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
//        }

    }

    fun getProductById(
        product_id: Int,
        onResult: (Boolean, ProductResponseItem?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var response = productRepository.getProductById(product_id)
                response.enqueue(object : Callback<ProductResponseItem> {
                    override fun onResponse(
                        call: Call<ProductResponseItem>,
                        response: Response<ProductResponseItem>
                    ) {
                        if (response.isSuccessful) {
                            val productResponse = response.body()
                            if (productResponse != null) {
                                onResult(true, productResponse)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }
                    override fun onFailure(call: Call<ProductResponseItem>, t: Throwable) {
                        onResult(false, null)
                    }
                })
            } catch (e: Exception) {
                onResult(false, null)
            }
        }
    }
}