package com.example.food_app.data.api

import com.example.food_app.data.model.Customer.AccountResponse
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username :String,
        @Field("password") password :String
    ):Call<LoginResponse>

    @GET("account/{username}")
    fun isExistAccount(
        @Path("username") username :String,
    ):Call<IsExistResponse>

    @FormUrlEncoded
    @POST("customer")
    fun signUp(
        @Field("full_name") full_name :String,
        @Field("email") email :String,
        @Field("phone_number") phone_number :String,
        @Field("username") username :String,
        @Field("password") password :String
    ):Call<IsExistResponse>

    @FormUrlEncoded
    @POST("customer/google")
    fun signUpGoogle(
        @Field("full_name") full_name :String,
        @Field("email") email :String,
        @Field("phone_number") phone_number :String,
        @Field("image_url") image_url :String,
        @Field("social_link") social_link :String,
    ):Call<Int>

    @FormUrlEncoded
    @POST("verify")
    fun isCorrectOTP(
        @Field("username") username :String,
        @Field("verifyCode") verifyCode :String
    ):Call<IsExistResponse>

    @FormUrlEncoded
    @POST("resend")
    fun resendOtp(
        @Field("customer_id") customer_id :Int,
        @Field("email") email :String
    ):Call<IsExistResponse>

    @FormUrlEncoded
    @POST("account/getByName")
    fun getAccountByName(
        @Field("username") username :String,
    ):Call<AccountResponse>

    @FormUrlEncoded
    @POST("recoveryPass")
    fun resendPass(
        @Field("username") username :String,
    ):Call<IsExistResponse>

    @FormUrlEncoded
    @POST("account/changePass")
    fun changePassword(
        @Field("id") id :Int,
        @Field("oldPass") oldPass :String,
        @Field("newPass") newPass :String
    ):Call<IsExistResponse>
}