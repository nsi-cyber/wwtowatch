package data.model.searchResultList

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultListResponse(
    val page: Int,
    val results: List<SearchResultItem>,
    val total_pages: Int,
    val total_results: Int
)