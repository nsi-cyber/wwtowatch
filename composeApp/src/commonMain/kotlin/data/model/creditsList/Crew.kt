package data.model.creditsList

import kotlinx.serialization.Serializable

@Serializable

data class Crew(
    val id: Int?=null,
    val job: String?=null,
    val name: String?=null,

    val popularity: Double?=null,
    val profile_path: String?=null
)