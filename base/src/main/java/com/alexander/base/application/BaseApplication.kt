package com.alexander.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.CallSuper
import com.alexander.base.data.local.ApplicationLanguage
import com.alexander.base.di.MainModule
import com.alexander.base.utils.LocaleHelper
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import org.koin.core.module.Module
import timber.log.Timber
import java.util.*

abstract class BaseApplication : Application() {

    abstract val modules: ArrayList<Module>
    abstract val baseUrl: String
    abstract val sharedPreferencesName: String
    abstract val isDebug: Boolean
    abstract val interceptors: List<Interceptor>
    private lateinit var koin: KoinApplication

    private var language: String = if (Locale.getDefault().language.equals(ApplicationLanguage.ENGLISH, true)) {
        ApplicationLanguage.ENGLISH
    } else {
        ApplicationLanguage.ARABIC
    }

    private fun initKoin() {
        val modules = MainModule.initApplicationModule(
                baseUrl,
                sharedPreferencesName,
                isDebug,
                interceptors
        )

        modules.addAll(this.modules)

        koin = startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(modules)
        }

        koin.logger(object : org.koin.core.logger.Logger() {
            override fun log(level: Level, msg: MESSAGE) {
                when (level) {
                    Level.DEBUG -> Timber.d(msg)
                    Level.INFO -> Timber.i(msg)
                    Level.ERROR -> Timber.e(msg)
                }
            }
        })
    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        if (isDebug) {
            Logger.addLogAdapter(AndroidLogAdapter())
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    Logger.log(priority, tag, message, t)
                }
            })
        }
        initKoin()
        LocaleHelper.onAttach(this)
    }

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration) {
        LocaleHelper.onAttach(this, language)
        super.onConfigurationChanged(newConfig)
    }

    @CallSuper
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!, language))
    }
}
