package com.alexander.cars.di

import com.alexander.cars.data.local.ILocalDataSource
import com.alexander.cars.data.local.LocalDataSource
import com.alexander.cars.data.remote.IRemoteDataSource
import com.alexander.cars.data.remote.RemoteDataSource
import com.alexander.cars.features.cars.data.repository.IMainRepository
import com.alexander.cars.features.cars.data.repository.MainRepository
import com.alexander.cars.features.cars.ui.MainViewModel
import com.alexander.cars.features.cars.usecase.GetCarsUseCase
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object FeaturesModules {
    val modules: ArrayList<Module>
        get() {
            val list = arrayListOf<Module>()

            // general
            list.add(module {
                // local data source
                single<ILocalDataSource> { LocalDataSource(get()) }
                // remote data source
                single<IRemoteDataSource> { RemoteDataSource(get()) }
                // repository
                single<IMainRepository> { MainRepository(get(), get()) }
                // use case
                factory { GetCarsUseCase(get()) }
                // view model
                viewModel<MainViewModel>()
            })
            return list
        }
}
