package com.example.food_app.data.Repository

import android.net.Uri
import android.util.Log
import com.example.food_app.data.api.CustomerApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.customerResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class CustomerRepository(
    private val customerApiService: CustomerApiService,
) {
    suspend fun getCustomer(customer_id: Int): Call<customerResponse> {
        return customerApiService.getCustomer(customer_id)
    }

    suspend fun updateInfo(
        id: Int,
        full_name: String,
        phone_number: String,
        email: String,
    ): Call<IsExistResponse> {
        return customerApiService.updateInfo(id, full_name, phone_number, email)
    }


    suspend fun uploadImage(id: Int, file: File): Boolean {
        return try {
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)
            customerApiService.uploadImage(id, imagePart) // Gọi phương thức Retrofit với tham số id và imagePart
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            false
        }
    }
}