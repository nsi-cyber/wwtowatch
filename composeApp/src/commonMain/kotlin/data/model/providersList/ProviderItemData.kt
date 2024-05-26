package data.model.providersList

import kotlinx.serialization.Serializable

@Serializable

data class ProviderItemData(
    val buy: List<Flatrate?>?=null,
    val flatrate: List<Flatrate?>?=null,
    val link: String?=null,
    val rent: List<Flatrate?>?=null
)