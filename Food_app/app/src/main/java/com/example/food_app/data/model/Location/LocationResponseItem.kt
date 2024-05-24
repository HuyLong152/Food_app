package com.example.food_app.data.model.Location

data class LocationResponseItem(
    val address: String,
    val created_time: String,
    val customer_id: Int,
    val id: Int,
    val is_default: Int,
    val name: String,
    val phone_number: String,
    val updated_time: String
)