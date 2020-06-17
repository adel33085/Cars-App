package com.alexander.cars.features.cars.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.alexander.cars.features.cars.domain.Car
import com.alexander.cars.features.cars.usecase.GetCarsUseCase
import com.alexander.cars.pagingUtils.NetworkState
import kotlinx.coroutines.runBlocking

class CarsPageKeyedDataSource(
    private val getCarsUseCase: GetCarsUseCase
) : PageKeyedDataSource<Int, Car>() {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialNetworkState = MutableLiveData<NetworkState>()

    fun retry() {
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Car>
    ) {
        networkState.postValue(NetworkState.LOADING)
        initialNetworkState.postValue(NetworkState.LOADING)
        runBlocking {
            when (val result = getCarsUseCase.invoke(0)) {
                is com.alexander.base.data.Result.Success -> {
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    initialNetworkState.postValue(NetworkState.LOADED)
                    callback.onResult(result.data, null, 2)
                }
                is com.alexander.base.data.Result.Error -> {
                    retry = {
                        loadInitial(params, callback)
                    }
                    val error = NetworkState.error(
                        errorMessageRes = result.exception.errorMessageRes,
                        errorMessage = result.exception.errorMessage
                    )
                    networkState.postValue(error)
                    initialNetworkState.postValue(error)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Car>) {
        networkState.postValue(NetworkState.LOADING)
        runBlocking {
            when (val result = getCarsUseCase.invoke(params.key + 10)) {
                is com.alexander.base.data.Result.Success -> {
                    retry = null
                    callback.onResult(result.data, params.key + 1)
                    networkState.postValue(NetworkState.LOADED)
                }
                is com.alexander.base.data.Result.Error -> {
                    retry = {
                        loadAfter(params, callback)
                    }
                    val error = NetworkState.error(
                        errorMessageRes = result.exception.errorMessageRes,
                        errorMessage = result.exception.errorMessage
                    )
                    networkState.postValue(error)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Car>) {}
}
