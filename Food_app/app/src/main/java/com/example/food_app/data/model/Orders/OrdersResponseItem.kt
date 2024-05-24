package com.example.food_app.data.model.Orders

import com.example.food_app.data.model.Product.Image
import com.example.food_app.data.model.Product.Review

data class OrdersResponseItem(
    val average_rate: Double,
    val calo: Int,
    val categorie: List<Any>,
    val created_time: String,
    val description: String,
    val discount: Int,
    val id: Int,
    val image: List<Image>,
    val ingredient: String,
    val name: String,
    val order_id: Int,
    val price: String,
    val quantity: Int,
    val review: List<Review>,
    val updated_time: String
)