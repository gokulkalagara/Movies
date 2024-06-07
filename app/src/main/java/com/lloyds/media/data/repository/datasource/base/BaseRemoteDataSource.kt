package com.lloyds.media.data.repository.datasource.base


import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import retrofit2.Response

/**
 * @Author: Gokul Kalagara
 *
 */
abstract class BaseRemoteDataSource {
    suspend fun <T> performSafeApiRequestCall(function: suspend () -> Response<T>):
            NetworkResult<T?, FailedResponse, Exception> {
        return try {
            val result = function.invoke()
            return when (result.isSuccessful) {
                true -> {
                    NetworkResult.Success(result.body())
                }

                else -> {
                    NetworkResult.Failed(FailedResponse(result.code(), result.errorBody()))
                }
            }
        } catch (e: java.lang.Exception) {
            NetworkResult.Error(e)
        }
    }
}