package domain.useCase

import data.model.movieDetail.MovieDetailResponse
import domain.repository.ImdbRepository


class GetMovieDetailUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(movieId:Int?): Result<MovieDetailResponse?> {
        return imdbRepository.getMovieDetail(movieId=movieId)
    }
}