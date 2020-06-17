package com.alexander.cars.pagingUtils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.network_state_list_item.view.*

class NetworkStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(retryCallback: () -> Unit, networkState: NetworkState?) {
        with(itemView) {
            loadingIndicator.visibility =
                if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
            retryBtn.visibility =
                if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
            errorMessageTV.visibility = if (networkState?.status == Status.FAILED) {
                val errorMessage = networkState.errorMessage
                if (errorMessage != null) {
                    errorMessageTV.text = errorMessage
                } else {
                    errorMessageTV.text = "unexpected error"
                }
                View.VISIBLE
            } else {
                View.GONE
            }
            retryBtn.setOnClickListener {
                retryCallback()
            }
        }
    }
}
