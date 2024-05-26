package data.model.providersList

import kotlinx.serialization.Serializable

@Serializable

data class Flatrate(
    val display_priority: Int?=null,
    val logo_path: String?=null,
    val provider_id: Int?=null,
    val provider_name: String?=null
)