package data.model.popularMoviesList

import kotlinx.serialization.Serializable

@Serializable
data class PopularMoviesListResponse(
    val page: Int?,
    val results: List<Result?>?,
    val total_pages: Int?,
    val total_results: Int?
)