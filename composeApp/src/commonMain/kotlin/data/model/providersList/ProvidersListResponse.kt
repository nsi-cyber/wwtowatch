package data.model.providersList

import kotlinx.serialization.Serializable

@Serializable

data class ProvidersListResponse(
    val id: Int,
    val results: ProvidersListResponseData?=null
)