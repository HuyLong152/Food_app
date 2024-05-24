package com.example.food_app.data.model.Auth

data class Account(
    val create_time: String,
    val customer_id: Int,
    val id: Int,
    val password: String,
    val role: String,
    val status: String,
    val update_time: String,
    val username: String,
    val verify_code: Any
)