package data.model.trendingList

import data.model.CardViewData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingListItem(

    @SerialName("id")


    val id: Int,
    @SerialName("media_type")

    val media_type: String?,

    @SerialName("name")
    val name: String? = null,
    @SerialName("title")

    val title: String? = null,

    @SerialName("overview")


    val overview: String,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("poster_path")
    val poster_path: String,
    @SerialName("vote_average")

    val vote_average: Double,

    @SerialName("vote_count")
    val vote_count: Int,
    @SerialName("release_date")

    val release_date: String?=null,
    @SerialName("first_air_date")

    val first_air_date: String?=null,



)

fun TrendingListItem.toCardViewData(): CardViewData {
    return CardViewData(
        id = id,
        media_type = media_type,
        title = when (media_type) {
            "movie" -> title
            "tv" -> name
            else -> null
        },
        poster_path = poster_path,
        vote_average = vote_average,
        date=when (media_type) {
            "movie" -> release_date
            "tv" -> first_air_date
            else -> null
        }
    )
}
