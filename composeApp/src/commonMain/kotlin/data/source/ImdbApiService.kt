package data.source

import data.Constants.BASE_LANGUAGE
import data.Constants.IMDB_BASE_URL
import data.model.BaseResponse
import data.model.movieDetail.MovieDetailResponse
import data.model.popularMoviesList.PopularMoviesListResponse
import data.model.showDetail.ShowDetailResponse
import data.model.topRatedShowsList.TopRatedShowsListResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.call.body
import io.ktor.client.request.parameter


class ImdbApiService(private val client: HttpClient) {



//Movies
    suspend fun getMovieDetail(movieId: Int): BaseResponse<MovieDetailResponse> {
        return client.get(IMDB_BASE_URL.plus("movie/${movieId}")){
            parameter("language", BASE_LANGUAGE)
        }.body()
    }

    suspend fun getPopularMovies(page: Int): PopularMoviesListResponse {
        return client.get(IMDB_BASE_URL.plus("movie/popular")){
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
        }.body()
    }



    //Shows
    suspend fun getShowDetail(showId: Int): BaseResponse<ShowDetailResponse> {
        return client.get(IMDB_BASE_URL.plus("tv/${showId}")){
            parameter("language", BASE_LANGUAGE)
        }.body()
    }

    suspend fun getTopRatedShows(page: Int): BaseResponse<TopRatedShowsListResponse> {
        return client.get(IMDB_BASE_URL.plus("tv/top_rated")){
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
        }.body()
    }




}