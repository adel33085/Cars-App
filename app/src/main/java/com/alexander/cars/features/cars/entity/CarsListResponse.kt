package com.alexander.cars.features.cars.entity

import com.google.gson.annotations.SerializedName

data class CarsListResponse(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("data")
    val data: List<CarResponse>?
)
