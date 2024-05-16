package data.model.popularMoviesList

import kotlinx.serialization.Serializable

@Serializable
data class PopularMoviesListResponse(
    val page: Int?,
    val results: List<PopularMovieItem?>?,
    val total_pages: Int?,
    val total_results: Int?
)