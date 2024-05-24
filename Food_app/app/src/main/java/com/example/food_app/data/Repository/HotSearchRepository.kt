package com.example.food_app.data.Repository

import com.example.food_app.data.api.HotSearchApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import retrofit2.Call

class HotSearchRepository(
    private val hotSearchApiService: HotSearchApiService
) {
    suspend fun create(
        text_search :String,
    ): Call<IsExistResponse> {
        return hotSearchApiService.create(text_search)
    }
}