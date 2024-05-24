package com.example.food_app.data.model.Product

data class ProductResponseItem(
    val average_rate: Double,
    val calo: Int,
    val categorie: List<Categorie>,
    val created_time: String,
    val description: String,
    val discount: Int,
    val id: Int,
    val image: List<Image>,
    val ingredient: String,
    val name: String,
    val price: String,
    val quantity: Int,
    val review: List<Review>,
    val sale_number: Int,
    val updated_time: String
)