package com.alexander.cars.features.cars.domain

data class Car(
    val id: Int,
    val brand: String,
    val constructionYear: Double?,
    val isUsed: Boolean,
    val imageUrl: String?
)
