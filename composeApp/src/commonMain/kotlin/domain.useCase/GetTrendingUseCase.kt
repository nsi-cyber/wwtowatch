package domain.useCase

import data.model.CardViewData
import data.model.trendingList.toCardViewData
import domain.repository.ImdbRepository


class GetTrendingUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(): Result<List<CardViewData?>?> {
        return imdbRepository.getTrending()
            .map { result -> result?.results?.filter { it.media_type != null && it.media_type != "person" }?.map { data -> data.toCardViewData() } }
    }
}