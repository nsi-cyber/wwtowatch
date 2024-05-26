package data.model.videosList

import kotlinx.serialization.Serializable

@Serializable

data class VideosListResponse(
    val id: Int,
    val results: List<VideosListResponseItem>
)