package presentation.exploreScreen

import androidx.compose.runtime.mutableStateOf
import data.repository.ImdbRepository


import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.CardViewData
import data.model.popularMoviesList.toCardViewData
import data.model.topRatedShowsList.toCardViewData
import data.model.trendingList.toCardViewData

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
                val updatedList = if (currentState is ListState.Content) {
                    val existingMovies = currentState.popularMovies.orEmpty().toMutableList()
                    existingMovies.addAll(movies)
                    existingMovies.toList()
                } else {
                    movies.toList()
                }
                // Check if the state has actually changed before updating
                if (mutableState.value !is ListState.Content) {
                    mutableState.update { ListState.Content(popularMovies = updatedList) }
                } else if (mutableState.value is ListState.Content && (mutableState.value as ListState.Content).popularMovies != updatedList) {
                    mutableState.update {
                        ListState.Content(
                            popularMovies = updatedList,
                            topRatedShows = (currentState as ListState.Content).topRatedShows,
                            topRatedMovies = (currentState as ListState.Content).topRatedMovies,
                            trendingAll = (currentState as ListState.Content).trendingAll
                        )
                    }
                }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            mutableState.update { ListState.ShowError(errorMessage) }
        }

        isLoadingPopularMovies.value = false
        currentPagePopularMovies.value += 1
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
                val updatedList = if (currentState is ListState.Content) {
                    val existingShows = currentState.topRatedShows.orEmpty().toMutableList()

                    existingShows.addAll(movies.map { it?.toCardViewData() })
                    existingShows.toList()
                } else {
                    movies.map { it?.toCardViewData() }.toList()
                }
                // Check if the state has actually changed before updating
                if (mutableState.value !is ListState.Content) {
                    mutableState.update { ListState.Content(topRatedShows = updatedList) }
                } else if (mutableState.value is ListState.Content && (mutableState.value as ListState.Content).topRatedShows != updatedList) {
                    mutableState.update {
                        ListState.Content(
                            popularMovies = (currentState as ListState.Content).popularMovies,
                            trendingAll = (currentState as ListState.Content).trendingAll,
                            topRatedMovies = (currentState as ListState.Content).topRatedMovies,
                            topRatedShows = updatedList
                        )
                    }
                }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage")
            mutableState.update { ListState.ShowError(errorMessage) }
        }

        isLoadingTopRatedShows.value = false
        currentPageTopRatedShows.value += 1
    }

    fun getTrending() = screenModelScope.launch {


        val result = imdbRepository.getTrending()
        val currentState = mutableState.value

        result.onSuccess { trending ->
            if (trending.isNullOrEmpty()) {
                mutableState.update { ListState.Empty }
            } else {
                val updatedList = if (currentState is ListState.Content) {
                    val existingTrends = currentState.trendingAll.orEmpty().toMutableList()

                    existingTrends.addAll(trending.map { it?.toCardViewData() })
                    existingTrends.toList()
                } else {
                    trending.map { it?.toCardViewData() }.toList()
                }
                updatedList.filter { it?.media_type != null && it.media_type != "person" }
                if (mutableState.value !is ListState.Content) {
                    mutableState.update { ListState.Content(trendingAll = updatedList) }
                } else if (mutableState.value is ListState.Content && (mutableState.value as ListState.Content).topRatedShows != updatedList) {
                    mutableState.update {
                        ListState.Content(
                            popularMovies = (currentState as ListState.Content).popularMovies,
                            topRatedShows = (currentState as ListState.Content).topRatedShows,
                            topRatedMovies = (currentState as ListState.Content).topRatedMovies,
                            trendingAll = updatedList
                        )
                    }
                }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            mutableState.update { ListState.ShowError(errorMessage) }
        }


    }
    fun getTopMovies() = screenModelScope.launch {


        val result = imdbRepository.getTopMovies(1)
        val currentState = mutableState.value

        result.onSuccess { movies ->
            if (movies.isNullOrEmpty()) {
                mutableState.update { ListState.Empty }
            } else {
                val updatedList = if (currentState is ListState.Content) {
                    val existingTrends = currentState.topRatedMovies.orEmpty().toMutableList()

                    existingTrends.addAll(movies.map { it?.toCardViewData() })
                    existingTrends.toList()
                } else {
                    movies.map { it?.toCardViewData() }.toList()
                }
                if (mutableState.value !is ListState.Content) {
                    mutableState.update { ListState.Content(topRatedMovies = updatedList) }
                } else if (mutableState.value is ListState.Content && (mutableState.value as ListState.Content).topRatedShows != updatedList) {
                    mutableState.update {
                        ListState.Content(
                            popularMovies = (currentState as ListState.Content).popularMovies,
                            topRatedShows = (currentState as ListState.Content).topRatedShows,
                            trendingAll = (currentState as ListState.Content).trendingAll,
                            topRatedMovies = updatedList
                        )
                    }
                }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            mutableState.update { ListState.ShowError(errorMessage) }
        }


    }

}

sealed interface ListState {
    data object Loading : ListState
    data object Empty : ListState
    data class Content(
        val popularMovies: List<CardViewData?>? = null,
        val topRatedShows: List<CardViewData?>? = null,
        val topRatedMovies: List<CardViewData?>? = null,
        val trendingAll: List<CardViewData?>? = null,
    ) : ListState

    data class ShowError(val message: String?) : ListState
}
