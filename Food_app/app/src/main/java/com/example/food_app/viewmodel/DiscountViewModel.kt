package com.example.food_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.DiscountRepository
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Discount.DiscountResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscountViewModel(
    private val discountRepository : DiscountRepository

):ViewModel() {


//    var isShowedDiscount = false
    fun isExistDiscount (
        discountCode :String,
        onResult : (Boolean, DiscountResponse?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = discountRepository.isExistDiscount(discountCode)
                response.enqueue(object : Callback<DiscountResponse> {
                    override fun onResponse(call: Call<DiscountResponse>, response: Response<DiscountResponse>) {
                        if(response.isSuccessful){
                            val discountResponse = response.body()
                            if (discountResponse != null){
                                onResult(true,discountResponse)
                            }
                        }else{
                            onResult(false,null)
                        }
                    }

                    override fun onFailure(call: Call<DiscountResponse>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }
    fun getNewDiscount (
        onResult : (Boolean, DiscountResponse?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = discountRepository.getNewDiscount()
                response.enqueue(object : Callback<DiscountResponse> {
                    override fun onResponse(call: Call<DiscountResponse>, response: Response<DiscountResponse>) {
                        if(response.isSuccessful){
                            val discountResponse = response.body()
                            if (discountResponse != null){
                                onResult(true,discountResponse)
                            }
                        }else{
                            onResult(false,null)
                        }
                    }

                    override fun onFailure(call: Call<DiscountResponse>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }
    fun checkUserUsed (
        discountCode :String,
        customer_id :Int,
        onResult : (Boolean,Int,String?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = discountRepository.checkUserUsed(discountCode,customer_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val discountResponse = response.body()
                            if (discountResponse!!.message == "Discount code already used by this customer"){
                                onResult(true,1,"Discount code already used by this customer")
                            }else if(discountResponse!!.message == "Discount code is available for this customer"){
                                onResult(true,2,"Discount code is available for this customer")
                            }else if(discountResponse!!.message == "Discount code has reached its usage limit"){
                                onResult(true,3,"Discount code has reached its usage limit")
                            }else if(discountResponse!!.message == "Discount code has expired"){
                                onResult(true,4,"Discount code has expired")
                            }
                        }else{
                            onResult(false,5,null)
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,5,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,5,null)
            }
        }
    }
}