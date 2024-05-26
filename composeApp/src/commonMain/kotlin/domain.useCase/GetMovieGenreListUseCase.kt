package domain.useCase

import data.model.genreKeywordList.Genre
import domain.repository.ImdbRepository


class GetMovieGenreListUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(): Result<List<Genre?>?> {
        return imdbRepository.getMovieGenreList().map { it?.genres }
    }
}