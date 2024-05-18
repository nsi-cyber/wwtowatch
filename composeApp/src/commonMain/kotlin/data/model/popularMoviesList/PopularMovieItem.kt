package data.model.popularMoviesList

import data.model.CardViewData
import data.model.trendingList.TrendingListItem
import kotlinx.serialization.Serializable

@Serializable

data class PopularMovieItem(
    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int?>?,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
)

fun PopularMovieItem.toCardViewData(): CardViewData {
    return CardViewData(
        id = id,
        media_type = "movie",
        title = title,
        poster_path = poster_path,
        vote_average = vote_average,
        date = release_date
    )
}
