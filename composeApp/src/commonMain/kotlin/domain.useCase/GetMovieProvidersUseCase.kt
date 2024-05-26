package domain.useCase

import data.model.providersList.ProvidersListResponse
import domain.repository.ImdbRepository


class GetMovieProvidersUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(movieId:Int?): Result<ProvidersListResponse?> {
        return imdbRepository.getMovieProviders(movieId)
    }
}