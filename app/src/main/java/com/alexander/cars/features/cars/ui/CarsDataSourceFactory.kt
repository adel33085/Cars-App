package com.alexander.cars.features.cars.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.alexander.cars.features.cars.domain.Car
import com.alexander.cars.features.cars.usecase.GetCarsUseCase

class CarsDataSourceFactory(
    private val getCarsUseCase: GetCarsUseCase
) : DataSource.Factory<Int, Car>() {

    val sourceLiveData = MutableLiveData<CarsPageKeyedDataSource>()

    override fun create(): DataSource<Int, Car> {
        val source = CarsPageKeyedDataSource(getCarsUseCase)
        sourceLiveData.postValue(source)
        return source
    }
}
