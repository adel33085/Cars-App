package com.alexander.cars.application

import com.alexander.base.application.BaseApplication
import com.alexander.cars.di.FeaturesModules
import okhttp3.Interceptor
import org.koin.core.module.Module
import java.util.*

class MyApplication : BaseApplication() {

    override val modules: ArrayList<Module> = FeaturesModules.modules
    override val baseUrl: String = "http://demo1286023.mockable.io/"
    override val sharedPreferencesName: String = "cars_shared_preferences"
    override val isDebug: Boolean = com.alexander.cars.BuildConfig.DEBUG
    override val interceptors: List<Interceptor> = emptyList()
}
