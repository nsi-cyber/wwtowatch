package data.model.creditsList

import kotlinx.serialization.Serializable

@Serializable

data class Cast(
    val character: String?=null,
    val gender: Int?=null,
    val id: Int?=null,
    val name: String?=null,
    val popularity: Double?=null,
    val profile_path: String?=null
)