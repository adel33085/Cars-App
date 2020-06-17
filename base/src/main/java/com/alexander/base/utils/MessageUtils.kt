package com.alexander.base.utils

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import com.alexander.base.R
import com.tapadoo.alerter.Alert
import com.tapadoo.alerter.Alerter

object MessageUtils {

    fun showSuccessMessage(activity: Activity, title: String, message: String): Alert? {
        return showMessage(
                activity = activity,
                title = title,
                message = message,
                backgroundColorRes = R.color.kellygreen,
                textAppearance = R.style.AlerterTextAppearance
        )
    }

    fun showErrorMessage(activity: Activity, title: String, message: String): Alert? {
        return showMessage(
                activity = activity,
                title = title,
                message = message,
                backgroundColorRes = R.color.red,
                textAppearance = R.style.AlerterTextAppearance
        )
    }

    private fun showMessage(
            activity: Activity,
            title: String,
            message: String,
            @ColorRes backgroundColorRes: Int?,
            @StyleRes textAppearance: Int?
    ): Alert? {
        val alerter = Alerter
                .create(activity)
                .setTitle(title)
                .setText(message)
        backgroundColorRes?.let { alerter.setBackgroundColorRes(it) }
        textAppearance?.let { alerter.setTextAppearance(it) }
        return alerter.show()
    }
}
