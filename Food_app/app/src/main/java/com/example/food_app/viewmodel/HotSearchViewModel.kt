package com.example.food_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.Repository.HotSearchRepository
import com.example.food_app.data.model.Auth.IsExistResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotSearchViewModel(
    private val hotSearchRepository : HotSearchRepository
):ViewModel() {
    fun create (
        text_search :String,
        onResult : (Boolean,String) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = hotSearchRepository.create(text_search)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val isExistResponse = response.body()
                            if (isExistResponse!!.message == "update hot search successful"){
                                onResult(true,isExistResponse.message)
                            }else if(isExistResponse!!.message == "add new hot search successful"){

                            }
                        }else{
                            onResult(false,response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,"Error")
                    }

                })
            }catch (e : Exception){
                onResult(false,e.message!!)
            }
        }
    }
}