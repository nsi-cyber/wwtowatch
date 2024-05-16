package presentation

import data.repository.ImdbRepository


import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.popularMoviesList.Result

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreScreenModel(
    private val imdbRepository: ImdbRepository
) : StateScreenModel<ListState>(ListState.Loading) {

    fun getMovies() = screenModelScope.launch {
        val currentState = mutableState.value

        imdbRepository.getPopularMovies(1).onSuccess { movies ->
            if (movies.results?.isEmpty() == true) {
                mutableState.update { ListState.Empty }
            } else {
                if (currentState is ListState.Content)
                    mutableState.update { currentState.copy(popularMovies = movies.results) }
                else
                    mutableState.update { ListState.Content(popularMovies = movies.results) }
            }
        }.onFailure { t ->
            mutableState.update { ListState.ShowError(t.message.orEmpty()) }
        }
    }


    fun getShows() = screenModelScope.launch {
        val currentState = mutableState.value

        imdbRepository.getTopRatedShows(1).onSuccess { shows ->
            if (shows.results.isEmpty() &&
                currentState !is ListState.Content
            ) {
                mutableState.update { ListState.Empty }
            } else {
                if (currentState is ListState.Content)
                    mutableState.update { currentState.copy(topRatedShows = shows.results) }
                else
                    mutableState.update { ListState.Content(topRatedShows = shows.results) }
            }
        }.onFailure { t ->
            mutableState.update { ListState.ShowError(t.message.orEmpty()) }
        }
    }
}


sealed interface ListState {
    data object Loading : ListState
    data object Empty : ListState
    data class Content(
        val popularMovies: List<data.model.popularMoviesList.Result?>? = null,
        val topRatedShows: List<data.model.topRatedShowsList.Result?>? = null,
        ) : ListState

    data class ShowError(val message: String?) : ListState
}