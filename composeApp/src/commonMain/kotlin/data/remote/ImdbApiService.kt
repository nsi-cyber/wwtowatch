package data.remote

import data.Constants.BASE_LANGUAGE
import data.Constants.IMDB_BASE_URL
import data.model.creditsList.CreditsListResponse
import data.model.genreKeywordList.GenreKeywordListResponse
import data.model.imageList.ImageListResponse
import data.model.movieDetail.MovieDetailResponse
import data.model.movieGenreList.MovieGenreListResponse
import data.model.popularMoviesList.PopularMoviesListResponse
import data.model.providersList.ProvidersListResponse
import data.model.searchResultList.SearchResultListResponse
import data.model.showDetail.ShowDetailResponse
import data.model.showGenreList.ShowGenreListResult
import data.model.topRatedShowsList.TopRatedShowsListResponse
import data.model.trendingList.TrendingListResponse
import data.model.videosList.VideosListResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter


class ImdbApiService : KtorClient() {


    //Movies
    suspend fun getMovieDetail(movieId: Int?): MovieDetailResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/${movieId}")) {
            parameter("language", BASE_LANGUAGE)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )
        }.body()
    }

    suspend fun getMovieCredits(movieId: Int?): CreditsListResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/${movieId}/credits")) {
            parameter("language", BASE_LANGUAGE)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )
        }.body()
    }

    suspend fun getMovieImages(movieId: Int?): ImageListResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/${movieId}/images")) {
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )
        }.body()
    }

    suspend fun getMovieSimilar(movieId: Int?): PopularMoviesListResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/${movieId}/similar")) {
            parameter("language", BASE_LANGUAGE)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )
        }.body()
    }

    suspend fun getMovieVideos(movieId: Int?): VideosListResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/${movieId}/videos")) {
            parameter("language", BASE_LANGUAGE)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )
        }.body()
    }

    suspend fun getMovieProviders(movieId: Int?): ProvidersListResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/${movieId}/watch/providers")) {
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )
        }.body()
    }


    suspend fun getPopularMovies(page: Int): PopularMoviesListResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/popular")) {
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }

    suspend fun getSearchResults(query: String, page: Int): SearchResultListResponse? {
        return client.get(IMDB_BASE_URL.plus("search/multi")) {
            parameter("query", query)
            parameter("page", page)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }


    //Shows
    suspend fun getShowDetail(showId: Int): ShowDetailResponse? {
        return client.get(IMDB_BASE_URL.plus("tv/${showId}")) {
            parameter("language", BASE_LANGUAGE)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }

    suspend fun getTopRatedShows(page: Int): TopRatedShowsListResponse? {
        return client.get(IMDB_BASE_URL.plus("tv/top_rated")) {
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }

    suspend fun getTrendingAll(): TrendingListResponse? {
        return client.get(IMDB_BASE_URL.plus("trending/all/week")) {
            parameter("language", BASE_LANGUAGE)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }


    suspend fun getMovieGenre(page: Int, genreId: Int): MovieGenreListResponse? {
        return client.get(IMDB_BASE_URL.plus("discover/movie")) {
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
            parameter("sort_by", "popularity.desc")
            parameter("with_genres", genreId)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }

    suspend fun getTvSeriesGenre(page: Int, genreId: Int): ShowGenreListResult? {
        return client.get(IMDB_BASE_URL.plus("discover/tv")) {
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
            parameter("sort_by", "popularity.desc")
            parameter("with_genres", genreId)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }


    suspend fun getTopMovies(page: Int): PopularMoviesListResponse? {
        return client.get(IMDB_BASE_URL.plus("movie/top_rated")) {
            parameter("language", BASE_LANGUAGE)
            parameter("page", page)
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }

    suspend fun getMovieGenreList(): GenreKeywordListResponse? {
        return client.get(IMDB_BASE_URL.plus("genre/movie/list")) {
            parameter("language", "en")
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }

    suspend fun getTvGenreList(): GenreKeywordListResponse? {
        return client.get(IMDB_BASE_URL.plus("genre/tv/list")) {
            parameter("language", "en")
            header(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og"
            )

        }.body()
    }


}