package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import com.example.food_app.data.model.Location.LocationResponse
import com.example.food_app.data.model.Location.LocationResponseItem
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LocationApiService {
    @GET("location/{customer_id}")
    fun getDefaultAddress(
        @Path("customer_id") customer_id :Int,
    ): Call<LocationResponseItem>

    @GET("location/id/{id}")
    fun getLocationById(
        @Path("id") id :Int,
    ): Call<LocationResponseItem>

    @GET("location/all/{customer_id}")
    fun getAllAddress(
        @Path("customer_id") customer_id :Int,
    ): Call<LocationResponse>

    @FormUrlEncoded
    @POST("location/add")
    fun addLocation(
        @Field("customer_id") customer_id :Int,
        @Field("name") name :String,
        @Field("address") address :String,
        @Field("phone_number") phone_number :String
    ):Call<IsExistResponse>

    @FormUrlEncoded
    @POST("location")
    fun updateLocation(
        @Field("id") id :Int,
        @Field("name") name :String,
        @Field("address") address :String,
        @Field("phone_number") phone_number :String,
    ):Call<IsExistResponse>

    @FormUrlEncoded
    @POST("location/updateDefault")
    fun updateDefault(
        @Field("id") id :Int,
        @Field("customer_id") customer_id: Int
    ):Call<IsExistResponse>

    @DELETE("location/{id}")
    fun deleteLocation(
        @Path("id") id :Int,
    ): Call<IsExistResponse>
}