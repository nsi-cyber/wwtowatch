package data.model.genreKeywordList

import kotlinx.serialization.Serializable

@Serializable

data class GenreKeywordListResponse(
    val genres: List<Genre>
)