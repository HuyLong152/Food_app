package com.example.food_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.ReviewReponsitory
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Product.ProductResponse
import com.example.food_app.data.model.Review.ReviewResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewViewModel(
    private val reviewRepository : ReviewReponsitory

):ViewModel() {
    val listAllReviewByIdLiveData: MutableLiveData<ReviewResponse> by lazy {
        MutableLiveData<ReviewResponse>()
    }

    fun getReviewById(
        product_id : Int,
        onResult: (Boolean, ReviewResponse?) -> Unit
    ) {
        val cachedProducts = listAllReviewByIdLiveData.value
        if (cachedProducts != null) {
            onResult(true, cachedProducts)
        } else {
            viewModelScope.launch {
                try {
                    var response = reviewRepository.getReviewById(product_id)

                    response.enqueue(object : Callback<ReviewResponse> {
                        override fun onResponse(
                            call: Call<ReviewResponse>,
                            response: Response<ReviewResponse>
                        ) {
                            if (response.isSuccessful) {
                                val reviewResponse = response.body()
                                if (reviewResponse != null) {
                                    listAllReviewByIdLiveData.value = reviewResponse
                                    onResult(true, reviewResponse)
                                }
                            } else {
                                onResult(false, null)
                            }
                        }

                        override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                            onResult(false, null)
                        }

                    })
                } catch (e: Exception) {
                    onResult(false, null)
                }
            }
        }
    }

    fun postReview (
        order_id :Int,
        product_id :Int,
        customer_id: Int,
        rate :Int,
        content : String?,
        onResult : (Boolean,String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = reviewRepository.postReview(order_id,product_id,customer_id,rate,content)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Add review successfully"){
                                onResult(true,isExistResponse.message)
                            }
                        }else{
                            onResult(false,"Add review fail")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Add review fail")
                    }

                })
            }catch (e : Exception){
                onResult(false,e.message!!)
            }
        }
    }


    fun updateContent (
        id :Int,
        content : String?,
        onResult : (Boolean,String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = reviewRepository.updateContent(id,content)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "update review successfully"){
                                onResult(true,isExistResponse.message)
                            }
                        }else{
                            onResult(false,"update review fail")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"update review fail")
                    }

                })
            }catch (e : Exception){
                onResult(false,e.message!!)
            }
        }
    }


    fun deleteReview (
        id :Int,
        onResult : (Boolean,String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = reviewRepository.deleteReview(id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "Review delete successful"){
                                onResult(true,isExistResponse.message)
                            }
                        }else{
                            onResult(false,"Failed to delete review")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Failed to delete review")
                    }

                })
            }catch (e : Exception){
                onResult(false,e.message!!)
            }
        }
    }
}