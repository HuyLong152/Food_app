package com.example.food_app.data.api

import android.net.Uri
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import com.example.food_app.data.model.Auth.customerResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomerApiService {
    @GET("customer/{id}")
    fun getCustomer(
        @Path("id") id :Int,
    ): Call<customerResponse>

    @FormUrlEncoded
    @PUT("customer")
    fun updateInfo(
        @Field("id") id: Int,
        @Field("full_name") full_name: String,
        @Field("phone_number") phone_number: String,
        @Field("email") email: String,
    ): Call<IsExistResponse>


    @Multipart
    @POST("customer/avatar")
    suspend fun uploadImage(
        @Query("id") id: Int, // Thêm tham số id vào đây
        @Part image : MultipartBody.Part
    )

}