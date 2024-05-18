package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CardViewData(

val id: Int?,

val media_type: String?,

val title: String?=null,

val poster_path: String?,

val vote_average: Double?,
val date: String?=null,

)
