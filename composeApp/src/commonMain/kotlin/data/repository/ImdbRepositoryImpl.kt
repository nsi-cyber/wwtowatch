package data.repository

import data.remote.ImdbApiService
import domain.repository.ImdbRepository


class ImdbRepositoryImpl(private val imdbApiService: ImdbApiService) : ImdbRepository {


    private suspend fun <T> safeApiCall(call: suspend () -> T): Result<T?> {
        return try {
            val response = call()
            if (response!=null) {
                Result.success(response)
            } else {
                Result.failure(Exception("Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetail(movieId: Int?) = safeApiCall {
        imdbApiService.getMovieDetail(movieId)
    }

    override suspend fun getMovieCredits(movieId: Int?) = safeApiCall {
        imdbApiService.getMovieCredits(movieId)
    }

    override suspend fun getMovieImages(movieId: Int?)= safeApiCall {
        imdbApiService.getMovieImages(movieId)
    }

    override suspend fun getMovieSimilar(movieId: Int?)= safeApiCall {
        imdbApiService.getMovieSimilar(movieId)
    }

    override suspend fun getMovieVideos(movieId: Int?)= safeApiCall {
        imdbApiService.getMovieVideos(movieId)
    }

    override suspend fun getMovieProviders(movieId: Int?)= safeApiCall {
        imdbApiService.getMovieProviders(movieId)
    }


    override suspend fun getPopularMovies(page: Int) = safeApiCall {
        imdbApiService.getPopularMovies(page)

    }

    override suspend fun getSearchResults(query: String, page: Int) = safeApiCall {
        imdbApiService.getSearchResults(query, page)

    }

    override suspend fun getShowDetail(showId: Int) = safeApiCall {
        imdbApiService.getShowDetail(showId)
    }


    override suspend fun getTopRatedShows(page: Int) = safeApiCall {
        imdbApiService.getTopRatedShows(page)
    }

    override suspend fun getTrending() = safeApiCall {
        imdbApiService.getTrendingAll()

    }

    override suspend fun getTopMovies(page: Int) = safeApiCall {
        imdbApiService.getTopMovies(page)

    }

    override suspend fun getTvGenreList() = safeApiCall {
        imdbApiService.getTvGenreList()

    }

    override suspend fun getMovieGenreList() = safeApiCall {
        imdbApiService.getMovieGenreList()

    }

    override suspend fun getMovieGenreDetailList(page: Int, genreId: Int) = safeApiCall {
        imdbApiService.getMovieGenre(page = page, genreId = genreId)

    }


    override suspend fun getShowGenreDetailList(page: Int, genreId: Int) = safeApiCall {

        imdbApiService.getTvSeriesGenre(page = page, genreId = genreId)

    }


}