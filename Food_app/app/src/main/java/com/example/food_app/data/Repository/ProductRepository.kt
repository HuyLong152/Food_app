package com.example.food_app.data.Repository

import com.example.food_app.data.api.ProductApiService
import com.example.food_app.data.model.Product.ProductResponse
import com.example.food_app.data.model.Product.ProductResponseItem
import retrofit2.Call

class ProductRepository(
    private val productApiService: ProductApiService
) {
    suspend fun getAllProduct(): Call<ProductResponse> {
        return productApiService.getAllProducts()
    }
    suspend fun getAllProductsByTime(time :Int): Call<ProductResponse> {
        return productApiService.getAllProductsByTime(time)
    }
    suspend fun getAllProductsByName(name :String): Call<ProductResponse> {
        return productApiService.getAllProductsByName(name)
    }
    suspend fun getPopularProducts(): Call<ProductResponse> {
        return productApiService.getPopularProducts()
    }
    suspend fun getProductsByPriceAndSort(): Call<ProductResponse> {
        return productApiService.getProductsByPriceAndSort()
    }
    suspend fun getProductsByRateAndSort(): Call<ProductResponse> {
        return productApiService.getProductsByRateAndSort()
    }
    suspend fun getProductsByTopSaleAndSort(): Call<ProductResponse> {
        return productApiService.getProductsByTopSaleAndSort()
    }
    suspend fun getProductsByCategory(category_id :Int): Call<ProductResponse> {
        return productApiService.getProductByCategory(category_id)
    }
    suspend fun getProductById(product_id :Int): Call<ProductResponseItem> {
        return productApiService.getProductById(product_id)
    }
    suspend fun getAllProductsByHotSearch(): Call<ProductResponse> {
        return productApiService.getAllProductsByHotSearch()
    }
    suspend fun getProductsRecommend(): Call<ProductResponse> {
        return productApiService.getProductsRecommend()
    }
}