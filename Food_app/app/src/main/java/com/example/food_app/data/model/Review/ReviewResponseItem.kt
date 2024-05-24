package com.example.food_app.data.model.Review

data class ReviewResponseItem(
    val content: String,
    val created_time: String,
    val customer_id: Int,
    val email: String,
    val full_name: String,
    val id: Int,
    val image_url: String,
    val phone_number: String,
    val product_id: Int,
    val rate: Int,
    val social_link: Any,
    val updated_time: String
)