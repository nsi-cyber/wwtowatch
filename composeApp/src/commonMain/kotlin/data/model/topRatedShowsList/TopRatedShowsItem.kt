package data.model.topRatedShowsList

import data.model.CardViewData
import kotlinx.serialization.Serializable

@Serializable
data class TopRatedShowsItem(
    val backdrop_path: String?,
    val first_air_date: String?,
    val genre_ids: List<Int?>?,
    val id: Int?,
    val name: String?,
    val origin_country: List<String?>?,
    val original_language: String?,
    val original_name: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val vote_average: Double?,
    val vote_count: Int?
)
fun TopRatedShowsItem.toCardViewData(): CardViewData {
    return CardViewData(
        id = id,
        media_type = "tv",
        title =name,
        poster_path = poster_path,
        vote_average = vote_average,
        date = first_air_date
    )
}
