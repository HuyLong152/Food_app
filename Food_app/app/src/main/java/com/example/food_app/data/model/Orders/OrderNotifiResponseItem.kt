package com.example.food_app.data.model.Orders

data class OrderNotifiResponseItem(
    val address: String,
    val created_time: String,
    val customer_id: Int,
    val id: Int,
    val name: String,
    val note: String,
    val payment: String,
    val payment_status: String,
    val phone_number: String,
    val status: String,
    val updated_time: String
)