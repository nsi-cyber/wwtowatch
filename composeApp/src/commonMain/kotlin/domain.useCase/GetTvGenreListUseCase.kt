package domain.useCase

import data.model.genreKeywordList.Genre
import domain.repository.ImdbRepository


class GetTvGenreListUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(): Result<List<Genre?>?> {
        return imdbRepository.getTvGenreList().map { it?.genres }
    }
}