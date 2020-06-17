package com.alexander.cars.features.cars.entity

import com.alexander.cars.features.cars.domain.Car
import com.google.gson.annotations.SerializedName

data class CarResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("constructionYear")
    val constructionYear: Double?,
    @SerializedName("isUsed")
    val isUsed: Boolean?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)

fun CarResponse.toCar(): Car? {
    if (id != null && brand != null && isUsed != null) {
        return Car(id, brand, constructionYear, isUsed, imageUrl)
    }
    return null
}
