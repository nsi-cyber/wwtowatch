package domain.useCase

import data.model.creditsList.CreditsListResponse
import domain.repository.ImdbRepository


class GetMovieCreditsUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(movieId:Int?): Result<CreditsListResponse?> {
        return imdbRepository.getMovieCredits(movieId=movieId)
    }
}