package com.alexander.base.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexander.base.data.ApplicationException
import com.alexander.base.data.Result
import com.alexander.base.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val error = MutableLiveData<Event<Result.Error>>()
    val loading = MutableLiveData<Event<Result.Loading>>().apply {
        value = Event(Result.Loading(false))
    }

    inline fun wrapBlockingOperation(
            showLoading: Boolean = true,
            crossinline apiCall: suspend CoroutineScope.() -> Unit
    ): Job {
        loading.value = Event(Result.Loading(showLoading))
        return viewModelScope.launch {
            try {
                apiCall()
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                if (throwable is ApplicationException) {
                    error.postValue(Event(Result.Error(throwable)))
                }
            } finally {
                loading.value = Event(Result.Loading(false))
            }
        }
    }

    fun <T> handleResult(result: Result<T>, onSuccess: (Result.Success<T>) -> Unit) {
        when (result) {
            is Result.Success<T> -> {
                onSuccess(result)
            }
            is Result.Error -> {
                throw result.exception
            }
        }
    }
}
