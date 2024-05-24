package com.example.food_app.data.api

import com.example.food_app.data.model.Auth.IsExistResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HotSearchApiService {
    @FormUrlEncoded
    @POST("hotsearch")
    fun create(
        @Field("text_search") text_search :String,
    ): Call<IsExistResponse>
}