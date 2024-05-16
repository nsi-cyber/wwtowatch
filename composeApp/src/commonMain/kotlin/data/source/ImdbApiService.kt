package data.source

import data.Constants.BASE_LANGUAGE
import data.Constants.IMDB_BASE_URL
import data.model.movieDetail.MovieDetailResponse
import data.model.popularMoviesList.PopularMoviesListResponse
import data.model.searchResultList.SearchResultListResponse
import data.model.showDetail.ShowDetailResponse
import data.model.topRatedShowsList.TopRatedShowsListResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.call.body
import io.ktor.client.request.parameter


class ImdbApiService(private val client: HttpClient) {



//Movies
    suspend fun getMovieDetail(movieId: Int): MovieDetailResponse {
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

    suspend fun getSearchResults(query:String,page: Int): SearchResultListResponse {
        return client.get(IMDB_BASE_URL.plus("search/multi")){
            parameter("query", query)
            parameter("page", page)
        }.body()
    }



    //Shows
    suspend fun getShowDetail(showId: Int):ShowDetailResponse {
        return client.get(IMDB_BASE_URL.plus("tv/${showId}")){
            parameter("language", BASE_LANGUAGE)
        }.body()
    }

    suspend fun getTopRatedShows(page: Int): TopRatedShowsListResponse {
        return client.get(IMDB_BASE_URL.plus("tv/top_rated")){
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
        }.body()
    }




}