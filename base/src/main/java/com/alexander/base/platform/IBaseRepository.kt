package com.alexander.base.platform

import com.alexander.base.data.Result
import retrofit2.Response

interface IBaseRepository {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T>
}
