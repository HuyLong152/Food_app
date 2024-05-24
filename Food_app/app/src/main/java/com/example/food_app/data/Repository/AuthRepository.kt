package com.example.food_app.data.Repository

import com.example.food_app.data.api.AuthApiService
import com.example.food_app.data.model.Customer.AccountResponse
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import retrofit2.Call

class AuthRepository(
    private val authApiService: AuthApiService
) {
    suspend fun login(username:String, password:String):Call<LoginResponse>{
        return authApiService.login(username,password)
    }
    suspend fun isExist(username:String):Call<IsExistResponse>{
        return authApiService.isExistAccount(username)
    }
    suspend fun signUp(
        full_name :String,
        email :String,
        phone_number :String,
        username :String,
        password :String
    ):Call<IsExistResponse>{
        return authApiService.signUp(full_name,email,phone_number,username,password)
    }

    suspend fun signUpGoogle(
        full_name :String,
        email :String,
        phone_number :String,
        image_url :String,
        social_link :String
    ):Call<Int>{
        return authApiService.signUpGoogle(full_name,email,phone_number,image_url,social_link)
    }

    suspend fun isCorrectOTP(username:String, verifyCode:String):Call<IsExistResponse>{
        return authApiService.isCorrectOTP(username,verifyCode)
    }
    suspend fun resendOtp(customer_id:Int, email:String):Call<IsExistResponse>{
        return authApiService.resendOtp(customer_id,email)
    }
    suspend fun getAccountByName(username:String):Call<AccountResponse>{
        return authApiService.getAccountByName(username)
    }
    suspend fun resendPass(username:String):Call<IsExistResponse>{
        return authApiService.resendPass(username)
    }
    suspend fun changePassword(id:Int, oldPass:String,newPass:String):Call<IsExistResponse>{
        return authApiService.changePassword(id,oldPass,newPass)
    }
}