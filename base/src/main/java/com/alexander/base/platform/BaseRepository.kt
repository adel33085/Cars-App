package com.alexander.base.platform

import com.alexander.base.R
import com.alexander.base.data.ApplicationException
import com.alexander.base.data.ErrorType
import com.alexander.base.data.Result
import com.alexander.base.data.local.IBaseLocalDataSource
import com.alexander.base.data.remote.IBaseRemoteDataSource
import com.alexander.base.utils.IConnectivityUtils
import com.alexander.base.utils.getKoinInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

abstract class BaseRepository<LocalData : IBaseLocalDataSource, RemoteData : IBaseRemoteDataSource>(
        val localDataSource: LocalData,
        val remoteDataSource: RemoteData
) : IBaseRepository {

    private val connectivityUtils by getKoinInstance<IConnectivityUtils>()

    private val noInternetError = Result.Error(ApplicationException(
            type = ErrorType.Network.NoInternetConnection,
            errorMessageRes = R.string.error_no_internet_connection
    ))

    private val unexpectedError = Result.Error(ApplicationException(
            type = ErrorType.Network.Unexpected,
            errorMessageRes = R.string.error_something_went_wrong
    ))

    override suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                if (connectivityUtils.isNetworkConnected.not()) {
                    return@withContext noInternetError
                }
                val response = call()
                return@withContext handleResult(response)
            } catch (error: Throwable) {
                Timber.e(error)
                unexpectedError
            }
        }
    }

    private fun <T : Any> handleResult(response: Response<T>): Result<T> {
        return when (response.code()) {
            200 -> {
                Result.Success(response.body()!!)
            }
            401 -> {
                Result.Error(ApplicationException(type = ErrorType.Network.Unauthorized))
            }
            404 -> {
                Result.Error(ApplicationException(type = ErrorType.Network.ResourceNotFound))
            }
            else -> {
                Result.Error(ApplicationException(type = ErrorType.Network.Unexpected))
            }
        }
    }
}
