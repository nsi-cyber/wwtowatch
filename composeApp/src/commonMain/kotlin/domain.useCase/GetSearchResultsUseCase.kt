package domain.useCase

import data.model.CardViewData
import data.model.searchResultList.toCardViewData
import domain.repository.ImdbRepository

class GetSearchResultsUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(query: String, page: Int): Result<List<CardViewData?>?> {
        return imdbRepository.getSearchResults(query = query, page = page).map { result ->
            result?.results?.filter { !it.media_type.isNullOrEmpty() && it.media_type != "person" }
                ?.map { data -> data.toCardViewData() }
        }
    }
}