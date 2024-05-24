package com.example.food_app.data.model.Auth

data class customerResponse(
    val created_time: String,
    val email: String,
    val full_name: String,
    val id: Int,
    val image_url: String,
    val phone_number: String,
    val social_link: Any,
    val updated_time: String
)