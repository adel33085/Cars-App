package com.alexander.cars.features.cars.data.repository

import com.alexander.base.data.ApplicationException
import com.alexander.base.data.ErrorType
import com.alexander.base.data.Result
import com.alexander.base.platform.BaseRepository
import com.alexander.cars.data.local.ILocalDataSource
import com.alexander.cars.data.remote.IRemoteDataSource
import com.alexander.cars.features.cars.domain.Car
import com.alexander.cars.features.cars.entity.toCar

class MainRepository(
    localDataSource: ILocalDataSource,
    remoteDataSource: IRemoteDataSource
) : BaseRepository<ILocalDataSource, IRemoteDataSource>(localDataSource, remoteDataSource),
    IMainRepository {

    override suspend fun getCars(page: Int): Result<List<Car>> {
        val result = safeApiCall {
            remoteDataSource.mainApi.getCars(page)
        }
        return when (result) {
            is Result.Success -> {
                Result.Success(result.data.data?.mapNotNull { it.toCar() }!!)
            }
            is Result.Error -> {
                result
            }
            else -> {
                Result.Error(ApplicationException(type = ErrorType.Unexpected))
            }
        }
    }
}
