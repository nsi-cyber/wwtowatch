package data.model.genreKeywordList

import kotlinx.serialization.Serializable

@Serializable

data class Genre(
    val id: Int,
    val name: String
)