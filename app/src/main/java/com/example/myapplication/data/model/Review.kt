package com.example.myapplication.data.model

data class Review(
    val id: String,
    val siteId: String,
    val userName: String,
    val rating: Float,
    val reviewText: String,
    val date: String
)
