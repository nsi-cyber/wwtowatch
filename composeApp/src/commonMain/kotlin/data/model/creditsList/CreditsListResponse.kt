package data.model.creditsList

import kotlinx.serialization.Serializable

@Serializable
data class CreditsListResponse(
    val cast: List<Cast?>?=null,
    val crew: List<Crew?>?=null,
    val id: Int?=null
)