package apk.zafar.wonderpic.model.network.vk

import apk.zafar.wonderpic.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VKApi {

    @GET("method/users.get")
    suspend fun getUser(
        @Query("access_token") token: String,
        @Query("user_ids") id: Int,
        @Query("fields") fields: String = "photo_max_orig",
        @Query("v") version: String = "5.52"
    ): Response<UserResponse>

}
