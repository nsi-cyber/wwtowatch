package data.model.topRatedShowsList

import kotlinx.serialization.Serializable

@Serializable
data class TopRatedShowsListResponse(
    val page: Int?,
    val results: List<TopRatedShowsItem?>?,
    val total_pages: Int?,
    val total_results: Int?
)