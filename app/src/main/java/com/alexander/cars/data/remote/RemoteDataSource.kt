package com.alexander.cars.data.remote

import com.alexander.cars.features.cars.data.api.MainApi
import retrofit2.Retrofit

class RemoteDataSource constructor(
    override val retrofit: Retrofit
) : IRemoteDataSource {

    override val mainApi: MainApi = retrofit.create(MainApi::class.java)
}
