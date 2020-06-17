package com.alexander.cars.data.remote

import com.alexander.base.data.remote.IBaseRemoteDataSource
import com.alexander.cars.features.cars.data.api.MainApi

interface IRemoteDataSource : IBaseRemoteDataSource {
    val mainApi: MainApi
}
