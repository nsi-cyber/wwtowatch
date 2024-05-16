package presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.repository.ImdbRepository


import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.popularMoviesList.PopularMovieItem
import data.model.topRatedShowsList.TopRatedShowsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class ExploreScreenModel(
    private val imdbRepository: ImdbRepository
) :
    StateScreenModel<ListState>(ListState.Loading) {

    private var currentPagePopularMovies = mutableStateOf(1)
    private var currentPageTopRatedShows = mutableStateOf(1)
    private var isLoadingPopularMovies = mutableStateOf(false)
    private var isLoadingTopRatedShows = mutableStateOf(false)

    fun getPopularMovies() = screenModelScope.launch {
        if (isLoadingPopularMovies.value) return@launch
        isLoadingPopularMovies.value = true

        val result = imdbRepository.getPopularMovies(currentPagePopularMovies.value)
        val currentState = mutableState.value

        result.onSuccess { movies ->
            if (movies.isNullOrEmpty()) {
                mutableState.update { ListState.Empty }
            } else {
                val updatedMovies = if (currentState is ListState.Content) {
                    val existingMovies = currentState.popularMovies.orEmpty().toMutableList()

                    existingMovies.addAll(movies)
                    existingMovies.toList()
                } else {
                    movies.toList()
                }
                // Check if the state has actually changed before updating
                if(mutableState.value !is ListState.Content) {
                    mutableState.update { ListState.Content(popularMovies = updatedMovies) }
                }
                else if(mutableState.value is ListState.Content && (mutableState.value as ListState.Content).popularMovies != updatedMovies) {
                    mutableState.update { ListState.Content(popularMovies = updatedMovies,topRatedShows = (currentState as ListState.Content).topRatedShows) }
                }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            mutableState.update { ListState.ShowError(errorMessage) }
        }

        isLoadingPopularMovies.value = false // Yüklenme tamamlandı
        currentPagePopularMovies.value+=1 // Yüklenme tamamlandı
    }


    fun getTopRatedShows() = screenModelScope.launch {
        if (isLoadingTopRatedShows.value) return@launch
        isLoadingTopRatedShows.value = true

        val result = imdbRepository.getTopRatedShows(currentPageTopRatedShows.value)
        val currentState = mutableState.value

        result.onSuccess { movies ->
            if (movies.isNullOrEmpty()) {
                mutableState.update { ListState.Empty }
            } else {
                val updatedShows = if (currentState is ListState.Content) {
                    val existingShows = currentState.topRatedShows.orEmpty().toMutableList()

                    existingShows.addAll(movies)
                    existingShows.toList()
                } else {
                    movies.toList()
                }
                // Check if the state has actually changed before updating
                if(mutableState.value !is ListState.Content) {
                    mutableState.update { ListState.Content(topRatedShows =updatedShows) }
                }
                else if(mutableState.value is ListState.Content && (mutableState.value as ListState.Content).topRatedShows != updatedShows) {
                    mutableState.update { ListState.Content(popularMovies =(currentState as ListState.Content).popularMovies ,topRatedShows =updatedShows) }
                }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            mutableState.update { ListState.ShowError(errorMessage) }
        }

        isLoadingTopRatedShows.value = false // Yüklenme tamamlandı
        currentPageTopRatedShows.value+=1 // Yüklenme tamamlandı
    }


}

sealed interface ListState {
    data object Loading : ListState
    data object Empty : ListState
    data class Content(
        val popularMovies: List<PopularMovieItem?>? = null,
        val topRatedShows: List<TopRatedShowsItem?>? = null,
    ) : ListState

    data class ShowError(val message: String?) : ListState
}
