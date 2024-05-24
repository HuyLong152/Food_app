package com.example.food_app.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.model.Customer.AccountResponse
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(
    private val authRepository : AuthRepository
): ViewModel() {
    fun login (
        username :String,
        password :String,
        onResult : (Int,String?, Int?) -> Unit,
    ){
        viewModelScope.launch {
            try{
                var response = authRepository.login(username,password)
                response.enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if(response.isSuccessful){
                            val loginResponse = response.body()
//                            Log.d("abc",loginResponse!!.message)

                            if (loginResponse!!.message == "Login successful"){
                                onResult(0,null,loginResponse.id_customer)
                            }else if(loginResponse!!.message == "Account has been block"){
                                onResult(1,loginResponse.message,null)
                            }else if(loginResponse!!.message == "Active account"){
                                onResult(2,loginResponse.message,loginResponse.id_customer)
                            }
                            else if(loginResponse!!.message == "Account do not exist"){
                                onResult(3,loginResponse.message,null)
                            }
                            else{
                                onResult(4,loginResponse.message,null)
                            }
                        }else{
                            onResult(5,response.code().toString() + ":" ,null)
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        onResult(5,t.message,null)
                    }

                })
            }catch (e : Exception){
                onResult(5,e.message!!,null)
            }
        }
    }

    fun isExist (
        username :String,
        onResult : (Boolean,String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = authRepository.isExist(username)
                response.enqueue(object : Callback<IsExistResponse>{
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Account already exists"){
                                onResult(true,isExistResponse.message)
                            }
                        }else{
                            onResult(false,response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Sign up fail!")
                    }

                })
            }catch (e : Exception){
                onResult(false,e.message!!)
            }
        }
    }

    fun signUp (
        full_name :String,
        email :String,
        phone_number :String,
        username :String,
        password :String,
        onResult : (Boolean,String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = authRepository.signUp(full_name,email,phone_number,username, password)
                response.enqueue(object : Callback<IsExistResponse>{
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Customer added successfully"){
                                onResult(true,isExistResponse.message)
                            }
                        }else{
                            onResult(false,response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Sign up fail!")
                    }

                })
            }catch (e : Exception){
                onResult(false,e.message!!)
            }
        }
    }

    fun signUpGoogle (
        full_name :String,
        email :String,
        phone_number :String,
        image_url :String,
        social_link :String,
        onResult : (Boolean,Int) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = authRepository.signUpGoogle(full_name,email,phone_number,image_url, social_link)
                response.enqueue(object : Callback<Int>{
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse != -1){
                                onResult(true,isExistResponse!!)
                            }
                        }else{
                            onResult(false,-1)
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        onResult(false,-1)
                    }

                })
            }catch (e : Exception){
                onResult(false,-1)
            }
        }
    }

    fun isCorrectOTP (
        username :String,
        verifyCode :String,
        onResult : (Int,String) -> Unit
    ){
        viewModelScope.launch {
//            Log.d("abc",username + verifyCode)
            try{
                var response = authRepository.isCorrectOTP(username, verifyCode)
                response.enqueue(object : Callback<IsExistResponse>{
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Verify success"){
                                onResult(0,isExistResponse.message)
                            }else if (isExistResponse!!.message == "Verify code is not correct"){
                                onResult(1,isExistResponse.message)
                            }else{
                                onResult(2,isExistResponse.message)
                            }
                        }else{
                            onResult(1,response.code().toString())

                        }
                    }
                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(1,"Sign up fail!")
                    }

                })
            }catch (e : Exception){
                onResult(1,e.message!!)
            }
        }
    }

    fun resendOtp (
        customer_id :Int,
        email :String,
        onResult : (Boolean,String) -> Unit
    ){
//        Log.d("abc",customer_id.toString() + email) // chua lay duoc email va customer_id
        viewModelScope.launch {
            try{
                var response = authRepository.resendOtp(customer_id, email)
                response.enqueue(object : Callback<IsExistResponse>{
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Resend OTP successful"){
                                onResult(true,isExistResponse.message)
                            }
                        }else{
                            onResult(false,response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Resend OTP fail!")
                    }

                })
            }catch (e : Exception){
                onResult(false,e.message!!)
            }
        }
    }

    fun getAccountByName (
        username :String,
        onResult : (Boolean, AccountResponse?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = authRepository.getAccountByName(username)
                response.enqueue(object : Callback<AccountResponse>{
                    override fun onResponse(call: Call<AccountResponse>, response: Response<AccountResponse>) {
                        if(response.isSuccessful){
                            val accountResponse = response.body()
                            if (accountResponse != null){
                                onResult(true,accountResponse)
                            }
                        }else{
                            onResult(false,null)
                        }
                    }

                    override fun onFailure(call: Call<AccountResponse>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }
    fun resendPass (
        username :String,
        onResult : (Boolean, String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = authRepository.resendPass(username)
                response.enqueue(object : Callback<IsExistResponse>{
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val accountResponse = response.body()
                            if (accountResponse!!.message == "Recovery password successful"){
                                onResult(true,"Recovery password successful")
                            }
                        }else{
                            onResult(false,"Recovery password fail")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Recovery password fail")
                    }

                })
            }catch (e : Exception){
                onResult(false,"Recovery password fail")
            }
        }
    }

    fun changePassword (
        id :Int,
        oldPass :String,
        newPass :String,
        onResult : (Boolean, String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = authRepository.changePassword(id, oldPass,newPass)
                response.enqueue(object : Callback<IsExistResponse>{
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val accountResponse = response.body()
                            if (accountResponse!!.message == "Password changed successfully"){
                                onResult(true,"Password changed successfully")
                            }
                        }else{
                            onResult(false,"Password changed fail")
                            Log.d("abc","sai")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Password changed fail")
                        Log.d("abc","sai1")
                    }

                })
            }catch (e : Exception){
                onResult(false,"Password changed fail")
            }
        }
    }
}