package com.zzang.chongdae.data.remote.util

import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

inline fun <T> safeApiCall(
    call: () -> Response<T>
): Result<T, DataError.Network> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            } ?: Result.Error(DataError.Network.NULL)
        } else {
            Result.Error(handleHttpError(response.code()))
        }
    } catch (e: IOException) {
        Result.Error(DataError.Network.CONNECTION_ERROR)
    } catch (e: HttpException) {
        Result.Error(handleHttpError(e.code()))
    } catch (e: Exception) {
        Result.Error(DataError.Network.UNKNOWN)
    }
}

fun handleHttpError(code: Int): DataError.Network {
    return when (code) {
        400 -> DataError.Network.BAD_REQUEST
        401 -> DataError.Network.UNAUTHORIZED
        403 -> DataError.Network.FORBIDDEN
        404 -> DataError.Network.NOT_FOUND
        409 -> DataError.Network.CONFLICT
        500 -> DataError.Network.SERVER_ERROR
        else -> DataError.Network.UNKNOWN
    }
}