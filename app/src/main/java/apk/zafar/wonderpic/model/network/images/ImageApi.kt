package apk.zafar.wonderpic.model.network.images

import apk.zafar.wonderpic.model.pojo.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int = 30
    ): Response<ImageResponse>

    @GET("curated")
    suspend fun fetchCurated(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int = 30
    ): Response<ImageResponse>

}