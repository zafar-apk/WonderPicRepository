package apk.zafar.wonderpic.model.network.images

import apk.zafar.wonderpic.di.ImagesApi
import apk.zafar.wonderpic.model.network.BaseRepository
import javax.inject.Inject

class ImagesRepository @Inject constructor(private val api: ImageApi) : BaseRepository() {

    suspend fun search(query: String, page: Int?) = safeApiCall { api.search(query, page) }

    suspend fun getCurated(page: Int?) = safeApiCall { api.fetchCurated(page) }

}