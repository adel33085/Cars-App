package com.alexander.cars.features.cars.usecase

import com.alexander.base.data.Result
import com.alexander.base.platform.BaseUseCase
import com.alexander.cars.features.cars.data.repository.IMainRepository
import com.alexander.cars.features.cars.domain.Car

class GetCarsUseCase(
    repository: IMainRepository
) : BaseUseCase<IMainRepository>(repository) {

    suspend operator fun invoke(page: Int): Result<List<Car>> {
        return repository.getCars(page)
    }
}
