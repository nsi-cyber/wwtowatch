package domain.useCase

import data.model.showDetail.ShowDetailResponse
import domain.repository.ImdbRepository


class GetShowDetailUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(showId: Int): Result<ShowDetailResponse?> {
        return imdbRepository.getShowDetail(showId = showId)
    }
}