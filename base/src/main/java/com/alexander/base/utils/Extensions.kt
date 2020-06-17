package com.alexander.base.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import org.koin.core.KoinComponent
import org.koin.core.inject

inline fun <reified T> getKoinInstance(): Lazy<T> {
    return lazy {
        return@lazy object : KoinComponent {
            val value: T by inject()
        }.value
    }
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}
