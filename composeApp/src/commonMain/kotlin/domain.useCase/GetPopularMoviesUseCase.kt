package domain.useCase

import data.model.CardViewData
import data.model.popularMoviesList.toCardViewData
import domain.repository.ImdbRepository


class GetPopularMoviesUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(page: Int): Result<List<CardViewData?>?> {
        return imdbRepository.getPopularMovies(page).map { result->result?.results?.map {data-> data?.toCardViewData() } }
    }
}