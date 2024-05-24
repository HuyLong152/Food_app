package com.example.food_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.CategoryRepository
import com.example.food_app.data.model.Category.CategoryResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel(
    private val categoryRepository : CategoryRepository
):ViewModel() {
    val listAllCategoryLiveData : MutableLiveData<CategoryResponse> by lazy{
        MutableLiveData<CategoryResponse>()
    }
    fun clearData(){
        listAllCategoryLiveData.value = null
    }
    fun getAllCategory (
        onResult : (Boolean,CategoryResponse?) -> Unit
    ){
        val cachedProducts = listAllCategoryLiveData.value
        if(cachedProducts != null){
            onResult(true,cachedProducts)
        }else {
            viewModelScope.launch {
                try {
                    var response = categoryRepository.getAllCategories()

                    response.enqueue(object : Callback<CategoryResponse> {
                        override fun onResponse(
                            call: Call<CategoryResponse>,
                            response: Response<CategoryResponse>
                        ) {
                            if (response.isSuccessful) {
                                val categoryResponse = response.body()
                                if (categoryResponse != null) {
                                    listAllCategoryLiveData.value = categoryResponse
                                    onResult(true, categoryResponse)
                                }
                            } else {
                                onResult(false, null)
                            }
                        }

                        override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                            onResult(false, null)
                        }

                    })
                } catch (e: Exception) {
                    onResult(false, null)
                }
            }
        }
    }
}