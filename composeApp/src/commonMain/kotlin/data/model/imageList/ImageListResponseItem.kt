package data.model.imageList

import kotlinx.serialization.Serializable

@Serializable

data class ImageListResponseItem(
    val aspect_ratio: Double,
    val file_path: String,
    val vote_count: Int,
)