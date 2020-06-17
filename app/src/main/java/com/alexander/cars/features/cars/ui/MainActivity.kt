package com.alexander.cars.features.cars.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.alexander.base.platform.BaseActivity
import com.alexander.cars.R
import com.alexander.cars.pagingUtils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_view.*

class MainActivity : BaseActivity<MainViewModel>() {

    private val adapter by lazy { CarsAdapter { viewModel.retry() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCars()

        tryAgainBtn.setOnClickListener {
            refresh()
        }

        swipeToRefreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun refresh() {
        viewModel.refresh()
        getCars()
    }

    private fun getCars() {
        carsRV.visibility = View.GONE
        loadingCL.visibility = View.GONE
        errorCL.visibility = View.GONE
        adapter.submitList(null)
        viewModel.getCars()
        viewModel.cars.observe(this, Observer {
            carsRV.visibility = View.VISIBLE
            carsRV.setHasFixedSize(true)
            adapter.submitList(it)
            carsRV.adapter = adapter
            if (swipeToRefreshLayout.isRefreshing) {
                swipeToRefreshLayout.isRefreshing = false
            }
        })
        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        viewModel.initialNetworkState.observe(this, Observer {
            if (it?.status == Status.RUNNING) {
                loadingCL.visibility = View.VISIBLE
            } else {
                loadingCL.visibility = View.GONE
            }
            carsRV.visibility = if (it?.status == Status.SUCCESS) View.VISIBLE else View.GONE
            errorCL.visibility = if (it?.status == Status.FAILED) {
                val errorMessage = it.errorMessage
                if (errorMessage != null) {
                    errorMessageTV.text = errorMessage
                } else {
                    errorMessageTV.text = "unexpected error"
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }
}
