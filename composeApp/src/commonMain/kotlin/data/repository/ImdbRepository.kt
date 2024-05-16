package data.repository

import data.model.movieDetail.MovieDetailResponse
import data.model.popularMoviesList.PopularMoviesListResponse
import data.model.showDetail.ShowDetailResponse
import data.model.topRatedShowsList.TopRatedShowsListResponse
import data.source.ImdbApiService


class ImdbRepository(private val imdbApiService: ImdbApiService) {

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetailResponse> {
        return try {
            val response = imdbApiService.getMovieDetail(movieId)
            if (response.status == 200 && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message.orEmpty()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getPopularMovies(page: Int): Result<PopularMoviesListResponse> {
        return try {
            val response = imdbApiService.getPopularMovies(page)
            if ( response != null) {
                Result.success(response)
            } else {
                Result.failure(Exception("response.message.orEmpty()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getShowDetail(showId: Int): Result<ShowDetailResponse> {
        return try {
            val response = imdbApiService.getShowDetail(showId)
            if (response.status == 200 && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message.orEmpty()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getTopRatedShows(page: Int) : Result<TopRatedShowsListResponse> {
        return try {
            val response = imdbApiService.getTopRatedShows(page)
            if (response.status == 200 && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message.orEmpty()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}