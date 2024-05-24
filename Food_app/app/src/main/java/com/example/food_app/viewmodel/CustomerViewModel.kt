package com.example.food_app.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.data.model.Product.ProductResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CustomerViewModel(
    private val customerRepository :CustomerRepository
):ViewModel() {
    val customerInfoLiveData : MutableLiveData<customerResponse> by lazy {
        MutableLiveData<customerResponse>()
    }

    fun clearData(){
        customerInfoLiveData.value = null
    }
//    fun getInforCustomer(cus_id :Int){
//        getCustomer(cus_id) { result, message, customerResponse ->
//            if (result) {
//                customerInfo = customerResponse!!
//            } else {
//
//            }
//        }
//    }
    fun getCustomer(
        customer_id :Int,
        onResult : (Boolean,String?,customerResponse?) -> Unit
    ){
//    val cachedProducts = customerInfoLiveData.value
//    if (cachedProducts != null) {
//        onResult(true, null,cachedProducts)
//    } else {
        viewModelScope.launch {
            try {
                var response = customerRepository.getCustomer(customer_id)
                response.enqueue(object : Callback<customerResponse> {
                    override fun onResponse(
                        call: Call<customerResponse>,
                        response: Response<customerResponse>
                    ) {
                        if (response.isSuccessful) {
                            val customerResponse = response.body()
//                            Log.d("abc",customerResponse.toString())
                            if (customerResponse != null) {
                                onResult(true, null, customerResponse)
                                customerInfoLiveData.value = customerResponse
                            }
                        } else {
                            onResult(false, response.code().toString(), null)
                        }
                    }

                    override fun onFailure(p0: Call<customerResponse>, p1: Throwable) {
                        TODO("Not yet implemented")
                    }

                })

            } catch (e: Exception) {
                onResult(false, e.message!!, null)
            }
        }
//    }
    }

    fun updateInfo(
        id :Int,
        full_name : String,
        phone_number :String,
        email :String,
        onResult : (Boolean,String?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = customerRepository.updateInfo(id,full_name,phone_number,email)
                response.enqueue(object : Callback<IsExistResponse>{
                    override fun onResponse(
                        call: Call<IsExistResponse>,
                        response: Response<IsExistResponse>
                    ) {
                        if(response.isSuccessful){
                            val customerResponse = response.body()
                            if (customerResponse!!.message == "Customer update successful"){
                                onResult(true,"Customer update successful")
                            }
                        }else{
                            onResult(false,"Failed to update Customer")
                        }
                    }

                    override fun onFailure(p0: Call<IsExistResponse>, p1: Throwable) {
                        TODO("Not yet implemented")
                    }

                })

            }catch(e : Exception){
                onResult(false,null)
            }
        }
    }



    fun updateImage(
        id :Int,
        file : File,
        onResult : (Boolean,String?) -> Unit
    ){
        viewModelScope.launch {
            try{

//                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
//                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)
                var response = customerRepository.uploadImage(id,file)
                if(response){
                    onResult(true,"save avatar successful")
                }else{
                    onResult(false,"save avatar fail")
                }
//                response.enqueue(object : Callback<IsExistResponse>{
//                    override fun onResponse(
//                        call: Call<IsExistResponse>,
//                        response: Response<IsExistResponse>
//                    ) {
//                        if(response.isSuccessful){
//                            val customerResponse = response.body()
//                            if (customerResponse!!.message == "save avatar successful"){
//                                onResult(true,"save avatar successful")
//                            }
//                        }else{
//                            onResult(false,"save avatar fail")
//                        }
//                        Log.d("abc","dươc roi")
//                    }
//
//                    override fun onFailure(p0: Call<IsExistResponse>, p1: Throwable) {
//                        TODO("Not yet implemented")
//                        Log.d("abc","chua dươc")
//
//                    }
//
//                })

            }catch(e : Exception){
                onResult(false,null)
                Log.d("abc",e.message.toString())
            }
        }
    }
}