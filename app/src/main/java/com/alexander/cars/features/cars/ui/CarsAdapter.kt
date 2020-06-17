package com.alexander.cars.features.cars.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexander.cars.R
import com.alexander.cars.features.cars.domain.Car
import com.alexander.cars.pagingUtils.NetworkState
import com.alexander.cars.pagingUtils.NetworkStateViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.car_list_item.view.*

class CarsAdapter(
    private val retryCallback: () -> Unit
) : PagedListAdapter<Car, RecyclerView.ViewHolder>(DiffCallback) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.car_list_item -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.car_list_item, parent, false)
                CarViewHolder(view)
            }
            R.layout.network_state_list_item -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.network_state_list_item, parent, false)
                NetworkStateViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("unknown view type $viewType")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_list_item
        } else {
            R.layout.car_list_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.car_list_item -> {
                (holder as CarViewHolder).bind(getItem(position))
            }
            R.layout.network_state_list_item -> {
                (holder as NetworkStateViewHolder).bind(retryCallback, networkState)
            }
        }
    }
}

object DiffCallback : DiffUtil.ItemCallback<Car>() {
    override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
        return oldItem == newItem
    }
}

class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(car: Car?) {
        with(itemView) {
            car?.let {
                Glide.with(photoImgV).load(car.imageUrl).into(photoImgV)
                brandTV.text = car.brand
                constructionYearTV.text = "${car.constructionYear}"
                if (car.isUsed) {
                    isUsedTV.text = "is used"
                } else {
                    isUsedTV.text = "new"
                }
            }
        }
    }
}
