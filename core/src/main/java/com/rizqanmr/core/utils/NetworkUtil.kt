package com.rizqanmr.core.utils

import com.rizqanmr.core.data.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> T,
) : Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall.invoke()
            Result.Success(response)
        } catch (throwable: Throwable) {
            when(throwable){
                is HttpException -> {
                    Result.Error(throwable)
                }
                else -> {
                    Result.Error(throwable)
                }
            }
        }
    }
}