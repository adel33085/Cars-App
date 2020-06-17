package com.alexander.cars.features.cars.data.api

import com.alexander.cars.features.cars.entity.CarsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {

    @GET("v1/cars")
    suspend fun getCars(
        @Query("page") page: Int
    ): Response<CarsListResponse>
}
