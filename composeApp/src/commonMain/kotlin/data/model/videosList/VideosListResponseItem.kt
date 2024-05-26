package data.model.videosList

import kotlinx.serialization.Serializable

@Serializable

data class VideosListResponseItem(
    val id: String,
    val key: String,
    val name: String,
    val official: Boolean,
    val published_at: String,
    val site: String,
    val size: Int,
    val type: String
)