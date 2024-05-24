package com.example.food_app.data.model.Product

data class Review(
    val content: String,
    val created_time: String,
    val customer_id: Int,
    val id: Int,
    val product_id: Int,
    val rate: Int,
    val updated_time: String
)