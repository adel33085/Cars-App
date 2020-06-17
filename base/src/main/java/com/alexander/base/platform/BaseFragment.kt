package com.alexander.base.platform

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alexander.base.data.Result
import com.alexander.base.utils.EventObserver
import com.alexander.base.utils.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<ViewModel : BaseViewModel> : Fragment() {

    @Suppress("UNCHECKED_CAST")
    val viewModel: ViewModel by lazy {
        getViewModel(((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<ViewModel>).kotlin)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loading.observe(this, EventObserver {
            hideKeyboard(requireActivity())
            if (it.loading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        viewModel.error.observe(this, EventObserver {
            hideKeyboard(requireActivity())
            hideLoading()
            showError(it)
        })
    }

    abstract fun showLoading()

    abstract fun hideLoading()

    abstract fun showError(error: Result.Error)
}
