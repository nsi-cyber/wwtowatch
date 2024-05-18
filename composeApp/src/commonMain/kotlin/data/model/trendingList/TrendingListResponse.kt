package data.model.trendingList

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingListResponse(
    @SerialName("page")
    val page: Int?,
    @SerialName("results")
    val results: List<TrendingListItem>,
    @SerialName("total_pages")
    val total_pages: Int?,
    @SerialName("total_results")
    val total_results: Int?
)