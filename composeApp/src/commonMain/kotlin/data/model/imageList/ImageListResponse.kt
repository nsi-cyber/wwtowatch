package data.model.imageList

import kotlinx.serialization.Serializable

@Serializable

data class ImageListResponse(

    val backdrops: List<ImageListResponseItem?>?=null
)