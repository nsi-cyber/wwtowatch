package presentation.detailScreen.movieDetailScreen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.CardViewData
import data.model.creditsList.CreditsListResponse
import data.model.imageList.ImageListResponseItem
import data.model.movieDetail.MovieDetailResponse
import data.model.providersList.ProvidersListResponseData
import data.model.videosList.VideosListResponseItem
import domain.useCase.GetMovieCreditsUseCase
import domain.useCase.GetMovieDetailUseCase
import domain.useCase.GetMovieImagesUseCase
import domain.useCase.GetMovieProvidersUseCase
import domain.useCase.GetMovieSimilarUseCase
import domain.useCase.GetMovieVideosUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MovieDetailScreenModel(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,//
    private val getMovieVideosUseCase: GetMovieVideosUseCase,
    private val getMovieSimilarUseCase: GetMovieSimilarUseCase,//
    private val getMovieProvidersUseCase: GetMovieProvidersUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,//


    ) :
    StateScreenModel<MovieDetailState>(MovieDetailState.Loading()) {


    fun getMovieDetail(movieId: Int?) = screenModelScope.launch {
        val result = getMovieDetailUseCase(movieId)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            updateLoadingOrEmptyState { it?.copy(details = successResult) }

        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching trending: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(details = null) }
        }
    }


    fun getMovieVideos(movieId: Int?) = screenModelScope.launch {
        val result = getMovieVideosUseCase(movieId)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            updateLoadingOrEmptyState { it?.copy(videos = successResult?.results) }

        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching trending: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(videos = listOf()) }
        }
    }

    fun getMovieSimilar(movieId: Int?) = screenModelScope.launch {
        val result = getMovieSimilarUseCase(movieId)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            updateLoadingOrEmptyState { it?.copy(similars = successResult) }

        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching trending: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(similars = listOf()) }
        }
    }

    fun getMovieProviders(movieId: Int?) = screenModelScope.launch {
        val result = getMovieProvidersUseCase(movieId)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            updateLoadingOrEmptyState { it?.copy(watchProviders = successResult?.results) }

        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching trending: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(watchProviders = null) }
        }
    }

    fun getMovieCredits(movieId: Int?) = screenModelScope.launch {
        val result = getMovieCreditsUseCase(movieId)

        result.onSuccess { successResult ->
            updateLoadingOrEmptyState { it?.copy(credits = successResult) }

        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching trending: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(credits = null) }
        }
    }


    fun getMovieImages(movieId: Int?) = screenModelScope.launch {
        val result = getMovieImagesUseCase(movieId)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            updateLoadingOrEmptyState { it?.copy(images = successResult?.backdrops?.sortedByDescending { it?.vote_count }) }

        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching trending: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(images = listOf()) }
        }
    }


    private fun updateLoadingOrEmptyState(update: (MovieDetailScreenData?) -> MovieDetailScreenData?) {
        if (mutableState.value is MovieDetailState.Loading) {
            val updatedData = update((mutableState.value as MovieDetailState.Loading).data)
            mutableState.update { MovieDetailState.Loading(data = updatedData) }
            if (updatedData.isContent()) {
                mutableState.update { MovieDetailState.Content(data = updatedData) }
            }
        } else {
            val updatedData = update((mutableState.value as MovieDetailState.Content).data)
            mutableState.update { MovieDetailState.Content(data = updatedData) }
            if (updatedData.isContent()) {
                mutableState.update { MovieDetailState.Content(data = updatedData) }
            }
        }
    }



}

sealed interface MovieDetailState {
    data class Loading(
        val data: MovieDetailScreenData? = MovieDetailScreenData()
    ) : MovieDetailState

    data class Content(
        val data: MovieDetailScreenData? = MovieDetailScreenData()
    ) : MovieDetailState

}

data class MovieDetailScreenData(
    val details: MovieDetailResponse? = null,
    val videos: List<VideosListResponseItem?>? = null,
    val credits: CreditsListResponse? = null,
    val images:  List<ImageListResponseItem?>? = null,
    val watchProviders: ProvidersListResponseData? = null,
    val similars: List<CardViewData?>? = null,
)


fun MovieDetailScreenData?.isContent(): Boolean {
    return this?.details != null
}


