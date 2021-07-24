package apk.zafar.wonderpic.model.network

import retrofit2.Response

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class NetworkError(val exception: Exception) : Result<Nothing>()
}

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = call()
            Result.Success(response.body()!!)
        } catch (e: Exception) {
            Result.NetworkError(e)
        }
    }
}




