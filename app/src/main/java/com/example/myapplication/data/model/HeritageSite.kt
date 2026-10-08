package com.example.myapplication.data.model

data class HeritageSite(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val category: String,
    val imageUrl: String,
    val builtYear: String,
    val isFavorite: Boolean = false,
    val averageRating: Float = 0f,
    val reviewCount: Int = 0
)
