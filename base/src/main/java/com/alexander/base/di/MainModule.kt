package com.alexander.base.di

import com.alexander.base.data.local.ISharedPreferences
import com.alexander.base.data.local.SharedPreferences
import com.alexander.base.data.remote.Network
import com.alexander.base.utils.ConnectivityUtils
import com.alexander.base.utils.IConnectivityUtils
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object MainModule {

    private val modules = arrayListOf<Module>()
    private const val BASE_URL = "base_url"
    private const val SHARED_PREFERENCES_NAME = "shared_preferences_name"
    private const val IS_DEBUG = "is_debug"
    private const val INTERCEPTORS = "interceptors"

    fun initApplicationModule(
            baseUrl: String,
            sharedPreferencesName: String,
            isDebug: Boolean,
            interceptors: List<Interceptor>
    ): ArrayList<Module> {

        // application configuration module
        modules.add(module {
            single(named(BASE_URL)) { baseUrl }
            single(named(SHARED_PREFERENCES_NAME)) { sharedPreferencesName }
            single(named(IS_DEBUG)) { isDebug }
            single(named(INTERCEPTORS)) { interceptors }
            single<IConnectivityUtils> { ConnectivityUtils(androidContext()) }
        })

        // local data source module
        modules.add(module {
            single<ISharedPreferences> { SharedPreferences(get(), androidContext(), get(named(SHARED_PREFERENCES_NAME))) }
        })

        // remote data source module
        modules.add(module {
            single { Network.provideGson() }
            single { Network.provideOkHttp(get(named(IS_DEBUG)), get(named(INTERCEPTORS))) }
            single { Network.provideRetrofit(get(named(BASE_URL)), get(), get()) }
        })

        return modules
    }
}
