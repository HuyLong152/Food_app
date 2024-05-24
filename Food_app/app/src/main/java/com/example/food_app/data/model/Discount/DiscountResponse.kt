package com.example.food_app.data.model.Discount

data class DiscountResponse(
    val available: Int,
    val create_time: String,
    val discount_code: String,
    val discount_percent: Int,
    val expiration_time: String,
    val id: Int,
    val number_uses: Any,
    val update_time: String
)