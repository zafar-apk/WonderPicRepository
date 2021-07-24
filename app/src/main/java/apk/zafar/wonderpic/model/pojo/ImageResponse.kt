package apk.zafar.wonderpic.model.pojo

data class ImageResponse(
    val next_page: String?,
    val page: Int?,
    val photos: List<Photo>?,
    val total_results: Int?
)