package apk.zafar.wonderpic.model.network.vk

import apk.zafar.wonderpic.model.UserResponse
import apk.zafar.wonderpic.model.network.BaseRepository
import apk.zafar.wonderpic.model.network.Result
import com.vk.api.sdk.auth.VKAccessToken
import javax.inject.Inject


class VKRepository @Inject constructor(
    private val vkApi: VKApi
) : BaseRepository() {

    suspend fun saveToken(token: VKAccessToken): Result<UserResponse> {
        with(token) {
            return getUserInfo(accessToken, userId!!)
        }
    }

    private suspend fun getUserInfo(token: String, id: Int): Result<UserResponse> {
        return safeApiCall { vkApi.getUser(token, id) }
    }

}
