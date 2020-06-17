package com.alexander.cars.pagingUtils

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val errorMessageRes: Int? = null,
    val errorMessage: String? = null
) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(errorMessageRes: Int? = null, errorMessage: String? = null): NetworkState {
            return NetworkState(Status.FAILED, errorMessageRes, errorMessage)
        }
    }
}

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}
