package com.example.food_app.data.model.Product

data class Categorie(
    val created_time: String,
    val id: Int,
    val image_url: String,
    val name: String,
    val pivot: Pivot,
    val updated_time: String
)