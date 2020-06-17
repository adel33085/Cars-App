package com.alexander.cars.features.cars.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alexander.base.platform.BaseViewModel
import com.alexander.cars.features.cars.domain.Car
import com.alexander.cars.features.cars.usecase.GetCarsUseCase
import com.alexander.cars.pagingUtils.NetworkState

class MainViewModel(
    private val getCarsUseCase: GetCarsUseCase
) : BaseViewModel() {

    lateinit var dataSourceFactory: CarsDataSourceFactory
    lateinit var cars: LiveData<PagedList<Car>>
    lateinit var initialNetworkState: LiveData<NetworkState>
    lateinit var networkState: LiveData<NetworkState>

    fun getCars() {
        val config = PagedList
            .Config
            .Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        dataSourceFactory = CarsDataSourceFactory(getCarsUseCase)
        cars = LivePagedListBuilder(dataSourceFactory, config).build()
        initialNetworkState =
            Transformations.switchMap(dataSourceFactory.sourceLiveData) { it.initialNetworkState }
        networkState =
            Transformations.switchMap(dataSourceFactory.sourceLiveData) { it.networkState }
    }

    fun retry() {
        dataSourceFactory.sourceLiveData.value?.retry()
    }

    fun refresh() {
        dataSourceFactory.sourceLiveData.value?.invalidate()
    }
}
