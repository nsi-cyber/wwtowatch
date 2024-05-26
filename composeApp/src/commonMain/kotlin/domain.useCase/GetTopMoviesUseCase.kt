package domain.useCase

import data.model.CardViewData
import data.model.popularMoviesList.toCardViewData
import domain.repository.ImdbRepository

class GetTopMoviesUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(page: Int): Result<List<CardViewData?>?> {
        return imdbRepository.getTopMovies(page = page).map { result-> result?.results?.map { data-> data?.toCardViewData() } }
    }
}