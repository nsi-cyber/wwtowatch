package data.model.movieGenreList

import kotlinx.serialization.Serializable

@Serializable
data class MovieGenreListResponse(
    val page: Int?,
    val results: List<MovieGenreListItem?>?,
    val total_pages: Int?,
    val total_results: Int?
)