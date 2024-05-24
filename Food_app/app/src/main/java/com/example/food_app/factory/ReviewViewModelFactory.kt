package com.example.food_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_app.data.Repository.ReviewReponsitory
import com.example.food_app.data.api.RetrofitClient.reviewApiService
import com.example.food_app.data.api.ReviewApiService
import com.example.food_app.viewmodel.ReviewViewModel

class ReviewViewModelFactory(private val reviewRepository: ReviewApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            val repository = ReviewReponsitory(reviewApiService)
            return ReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}