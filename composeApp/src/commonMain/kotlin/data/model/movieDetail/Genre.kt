package data.model.movieDetail

import kotlinx.serialization.Serializable

@Serializable

data class Genre(
    val id: Int,
    val name: String
)