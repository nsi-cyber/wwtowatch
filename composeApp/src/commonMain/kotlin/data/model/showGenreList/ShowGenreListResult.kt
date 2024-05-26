package data.model.showGenreList

import kotlinx.serialization.Serializable

@Serializable

data class ShowGenreListResult(
    val page: Int?,
    val results: List<ShowGenreListItem?>?,
    val total_pages: Int?,
    val total_results: Int?
)