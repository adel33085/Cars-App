package com.alexander.base.platform

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alexander.base.data.Result
import com.alexander.base.utils.EventObserver
import com.alexander.base.utils.LocaleHelper
import com.alexander.base.utils.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<ViewModel : BaseViewModel> : AppCompatActivity() {

    @Suppress("UNCHECKED_CAST")
    val viewModel: ViewModel by lazy {
        getViewModel(((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<ViewModel>).kotlin)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocaleHelper.onAttach(this)

        viewModel.loading.observe(this, EventObserver {
            hideKeyboard(this)
            if (it.loading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        viewModel.error.observe(this, EventObserver {
            hideKeyboard(this)
            hideLoading()
            showError(it)
        })
    }

    fun showLoading() {}

    fun hideLoading() {}

    fun showError(error: Result.Error) {}

    override fun onConfigurationChanged(newConfig: Configuration) {
        LocaleHelper.onAttach(this)
        super.onConfigurationChanged(newConfig)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
    }
}
