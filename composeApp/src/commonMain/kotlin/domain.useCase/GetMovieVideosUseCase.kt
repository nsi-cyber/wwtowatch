package domain.useCase

import data.model.videosList.VideosListResponse
import domain.repository.ImdbRepository


class GetMovieVideosUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(movieId:Int?): Result<VideosListResponse?> {
        return imdbRepository.getMovieVideos(movieId=movieId)
    }
}