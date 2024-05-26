package presentation.exploreScreen


import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.CardViewData
import domain.useCase.GetPopularMoviesUseCase
import domain.useCase.GetTopMoviesUseCase
import domain.useCase.GetTopRatedShowsUseCase
import domain.useCase.GetTrendingUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreScreenModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedShowsUseCase: GetTopRatedShowsUseCase,
    private val getTrendingUseCase: GetTrendingUseCase,
    private val getTopMoviesUseCase: GetTopMoviesUseCase,
) :
    StateScreenModel<ListState>(ListState.Loading()) {

    private var currentPagePopularMovies = mutableStateOf(1)
    private var currentPageTopRatedShows = mutableStateOf(1)
    private var isLoadingPopularMovies = mutableStateOf(false)
    private var isLoadingTopRatedShows = mutableStateOf(false)


    fun getTopRatedShows() = screenModelScope.launch {
        if (isLoadingTopRatedShows.value) return@launch
        isLoadingTopRatedShows.value = true

        val result = getTopRatedShowsUseCase(currentPageTopRatedShows.value)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            if (successResult.isNullOrEmpty()) {
                updateLoadingOrEmptyState { it?.copy(topRatedShows = listOf()) }
            } else {
                val updatedList = if (currentState is ListState.Content) {
                    val existingShows = currentState.data?.topRatedShows.orEmpty().toMutableList()
                    existingShows.addAll(successResult)
                    existingShows
                } else {
                    successResult
                }
                updateStateWithNewData { it?.copy(topRatedShows = updatedList) }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching top rated shows: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(topRatedShows = listOf()) }
        }

        isLoadingTopRatedShows.value = false
        currentPageTopRatedShows.value += 1
    }

    fun getTrending() = screenModelScope.launch {
        val result = getTrendingUseCase()
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            if (successResult.isNullOrEmpty()) {
                updateLoadingOrEmptyState { it?.copy(trendingAll = listOf()) }
            } else {
                val updatedList = if (currentState is ListState.Content) {
                    val existingTrends = currentState.data?.trendingAll.orEmpty().toMutableList()
                    existingTrends.addAll(successResult)
                    existingTrends
                } else {
                    successResult
                }
                updateStateWithNewData { it?.copy(trendingAll = updatedList) }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching trending: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(trendingAll = listOf()) }
        }
    }

    fun getTopMovies() = screenModelScope.launch {
        val result = getTopMoviesUseCase(1)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            if (successResult.isNullOrEmpty()) {
                updateLoadingOrEmptyState { it?.copy(topRatedMovies = listOf()) }
            } else {
                val updatedList = if (currentState is ListState.Content) {
                    val existingMovies = currentState.data?.topRatedMovies.orEmpty().toMutableList()
                    existingMovies.addAll(successResult)
                    existingMovies
                } else {
                    successResult
                }
                updateStateWithNewData { it?.copy(topRatedMovies = updatedList) }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching top rated movies: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(topRatedMovies = listOf()) }
        }
    }


    fun getPopularMovies() = screenModelScope.launch {
        if (isLoadingPopularMovies.value) return@launch
        isLoadingPopularMovies.value = true

        val result = getPopularMoviesUseCase(currentPagePopularMovies.value)
        val currentState = mutableState.value

        result.onSuccess { successResult ->
            if (successResult.isNullOrEmpty()) {
                updateLoadingOrEmptyState { it?.copy(popularMovies = listOf()) }
            } else {
                val updatedList = if (currentState is ListState.Content) {
                    val existingMovies = currentState.data?.popularMovies.orEmpty().toMutableList()
                    existingMovies.addAll(successResult)
                    existingMovies
                } else {
                    successResult
                }
                updateStateWithNewData { it?.copy(popularMovies = updatedList) }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching popular movies: $errorMessage")
            updateLoadingOrEmptyState { it?.copy(popularMovies = listOf()) }
        }

        isLoadingPopularMovies.value = false
        currentPagePopularMovies.value += 1
    }

    private fun updateLoadingOrEmptyState(update: (ExploreScreenData?) -> ExploreScreenData?) {
        if (mutableState.value is ListState.Loading) {
            val updatedData = update((mutableState.value as ListState.Loading).data)
            mutableState.update { ListState.Loading(data = updatedData) }
            if (updatedData.isContent()) {
                mutableState.update { ListState.Content(data = updatedData) }
            }
        } else {
            val updatedData = update(null)
            mutableState.update { ListState.Loading(data = updatedData) }
            if (updatedData.isContent()) {
                mutableState.update { ListState.Content(data = updatedData) }
            }
        }
    }

    private fun updateStateWithNewData(update: (ExploreScreenData?) -> ExploreScreenData?) {
        if (mutableState.value is ListState.Loading) {
            val updatedData = update((mutableState.value as ListState.Loading).data)
            mutableState.update { ListState.Loading(data = updatedData) }
            if (updatedData.isContent()) {
                mutableState.update { ListState.Content(data = updatedData) }
            }
        } else {
            val updatedData = update((mutableState.value as ListState.Content).data)
            mutableState.update { ListState.Content(data = updatedData) }
        }
    }


}

sealed interface ListState {
    data class Loading(
        val data: ExploreScreenData? = ExploreScreenData()
    ) : ListState

    data class Content(
        val data: ExploreScreenData? = ExploreScreenData()
    ) : ListState

}

data class ExploreScreenData(
    val popularMovies: List<CardViewData?>? = null,
    val topRatedShows: List<CardViewData?>? = null,
    val topRatedMovies: List<CardViewData?>? = null,
    val trendingAll: List<CardViewData?>? = null,
)


fun ExploreScreenData?.isContent(): Boolean {
    return this?.trendingAll != null
}