package domain.useCase

import data.model.CardViewData
import data.model.topRatedShowsList.toCardViewData
import domain.repository.ImdbRepository

class GetTopRatedShowsUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(page: Int): Result<List<CardViewData?>?> {
        return imdbRepository.getTopRatedShows(page = page)
            .map { result -> result?.results?.map { data -> data?.toCardViewData() } }
    }
}