package domain.repository

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


interface ImdbRepository {

    suspend fun getMovieDetail(movieId: Int?): Result<MovieDetailResponse?>
    suspend fun getMovieCredits(movieId: Int?): Result<CreditsListResponse?>
    suspend fun getMovieImages(movieId: Int?): Result<ImageListResponse?>
    suspend fun getMovieSimilar(movieId: Int?): Result<PopularMoviesListResponse?>
    suspend fun getMovieVideos(movieId: Int?): Result<VideosListResponse?>
    suspend fun getMovieProviders(movieId: Int?): Result<ProvidersListResponse?>


    suspend fun getPopularMovies(page: Int): Result<PopularMoviesListResponse?>

    suspend fun getSearchResults(query: String, page: Int): Result<SearchResultListResponse?>

    suspend fun getShowDetail(showId: Int): Result<ShowDetailResponse?>


    suspend fun getTopRatedShows(page: Int): Result<TopRatedShowsListResponse?>
    suspend fun getTrending(): Result<TrendingListResponse?>


    suspend fun getTopMovies(page: Int): Result<PopularMoviesListResponse?>
    suspend fun getTvGenreList(): Result<GenreKeywordListResponse?>
    suspend fun getMovieGenreList(): Result<GenreKeywordListResponse?>

    suspend fun getMovieGenreDetailList(page: Int, genreId: Int): Result<MovieGenreListResponse?>

    suspend fun getShowGenreDetailList(page: Int, genreId: Int): Result<ShowGenreListResult?>

}
