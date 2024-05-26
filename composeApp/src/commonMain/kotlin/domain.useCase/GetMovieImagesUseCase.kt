package domain.useCase

import data.model.imageList.ImageListResponse
import domain.repository.ImdbRepository


class GetMovieImagesUseCase(
    private val imdbRepository: ImdbRepository
) {
    suspend operator fun invoke(movieId:Int?): Result<ImageListResponse?> {
        return imdbRepository.getMovieImages(movieId)
    }
}