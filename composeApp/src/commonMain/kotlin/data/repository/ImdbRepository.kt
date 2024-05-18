package data.repository

import data.model.CardViewData
import data.model.movieDetail.MovieDetailResponse
import data.model.popularMoviesList.PopularMovieItem
import data.model.popularMoviesList.PopularMoviesListResponse
import data.model.popularMoviesList.toCardViewData
import data.model.searchResultList.SearchResultItem
import data.model.showDetail.ShowDetailResponse
import data.model.topRatedShowsList.TopRatedShowsItem
import data.model.topRatedShowsList.TopRatedShowsListResponse
import data.model.trendingList.TrendingListItem
import data.source.ImdbApiService


class ImdbRepository(private val imdbApiService: ImdbApiService) {

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetailResponse> {
        return try {
            val response = imdbApiService.getMovieDetail(movieId)
            if (response != null) {
                Result.success(response)
            } else {
                Result.failure(Exception("response.message.orEmpty()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getPopularMovies(page: Int): Result<List<CardViewData?>?> {
        return try {
            val response = imdbApiService.getPopularMovies(page)
            if (response.results?.isEmpty() == false) {
                Result.success(response.results.map { it?.toCardViewData() })
            } else {
                Result.failure(Exception("response.message.orEmpty()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getSearchResults(query:String,page: Int): Result<List<SearchResultItem?>?> {
        return try {
            val response = imdbApiService.getSearchResults(query,page)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getShowDetail(showId: Int): Result<ShowDetailResponse> {
        return try {
            val response = imdbApiService.getShowDetail(showId)
            if ( response != null) {
                Result.success(response)
            } else {
                Result.failure(Exception("response.message.orEmpty()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getTopRatedShows(page: Int) : Result<List<TopRatedShowsItem?>?> {
        return try {
            val response = imdbApiService.getTopRatedShows(page)
            if (response.results?.isEmpty() == false) {
                Result.success(response.results)
            } else {
                Result.failure(Exception("response.message.orEmpty()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getTrending() : Result<List<TrendingListItem?>?> {
        return try {
            val response = imdbApiService.getTrendingAll()
            if (response.results?.isNullOrEmpty() == false) {
                Result.success(response.results)
            } else {
                Result.failure(Exception("response.message.orEmpty()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getTopMovies(page:Int) : Result<List<PopularMovieItem?>?> {
        return try {
            val response = imdbApiService.getTopMovies(page)
            if (response.results?.isNullOrEmpty() == false) {
                Result.success(response.results)
            } else {
                Result.failure(Exception("response.message.orEmpty()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}