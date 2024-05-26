package domain.useCase

import data.model.CardViewData
import data.model.popularMoviesList.toCardViewData
import domain.repository.ImdbRepository


class GetMovieSimilarUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(movieId: Int?): Result<List<CardViewData?>?> {
        return imdbRepository.getMovieSimilar(movieId = movieId)
            .map { result -> result?.results?.map { data -> data?.toCardViewData() } }
    }
}