package com.alexander.cars.features.cars.data.repository

import com.alexander.base.data.Result
import com.alexander.base.platform.IBaseRepository
import com.alexander.cars.features.cars.domain.Car

interface IMainRepository : IBaseRepository {

    suspend fun getCars(page: Int): Result<List<Car>>
}
