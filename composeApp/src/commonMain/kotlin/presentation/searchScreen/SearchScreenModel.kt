import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.CardViewData
import data.model.genreKeywordList.Genre
import domain.useCase.GetMovieGenreListUseCase
import domain.useCase.GetSearchResultsUseCase
import domain.useCase.GetTvGenreListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenModel(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val getMovieGenreListUseCase: GetMovieGenreListUseCase,
    private val getTvGenreListUseCase: GetTvGenreListUseCase,
) :
    StateScreenModel<SearchState>(SearchState.Loading(SearchScreenData())) {

     var isLoadingSearch = mutableStateOf(false)

    private var searchJob: Job? = null


    fun getMovieGenreList() = screenModelScope.launch {
        val result = getMovieGenreListUseCase()

        result.onSuccess { successResult ->
            if (successResult.isNullOrEmpty()) {
                updateState { it?.copy(movieGenreList = listOf()) }
            } else {
                updateState { it?.copy(movieGenreList = successResult) }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching getMovieGenreList: $errorMessage")
            updateState { it?.copy(movieGenreList = listOf()) }
        }
    }

    fun getTvGenreList() = screenModelScope.launch {
        val result = getTvGenreListUseCase()

        result.onSuccess { successResult ->
            if (successResult.isNullOrEmpty()) {
                updateState { it?.copy(tvGenreList = listOf()) }
            } else {
                updateState { it?.copy(tvGenreList = successResult) }
            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching getTvGenreList: $errorMessage")
            updateState { it?.copy(tvGenreList = listOf()) }
        }
    }

    fun getSearchResults(query: String) = screenModelScope.launch {
        isLoadingSearch.value = true

        val result = getSearchResultsUseCase(query, 1)

        result.onSuccess { successResult ->
            updateState { it?.copy(searchResult = successResult) }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            updateState { it?.copy(searchResult = listOf()) }
        }

        isLoadingSearch.value = false
    }

    fun searchWithDebounce(query: String) {
        searchJob?.cancel()
        searchJob = screenModelScope.launch {
            delay(500)
            getSearchResults(query)
        }
    }

    fun setStateEmpty() {
        searchJob?.cancel()
        if (mutableState.value is SearchState.Content)
            mutableState.update {
                SearchState.Empty(data = (mutableState.value as SearchState.Content).data)
            }
    }

    private fun updateState(update: (SearchScreenData?) -> SearchScreenData?) {
        if (mutableState.value is SearchState.Loading) {
            val updatedData = update((mutableState.value as SearchState.Loading).data)

                mutableState.update { SearchState.Empty(data = updatedData) }



        } else
            if (mutableState.value is SearchState.Content) {
                val updatedData = update((mutableState.value as SearchState.Content).data)
                mutableState.update { SearchState.Content(data = updatedData) }

            } else
                if (mutableState.value is SearchState.Empty) {
                    val updatedData = update((mutableState.value as SearchState.Empty).data)
                    mutableState.update { SearchState.Empty(data = updatedData) }
                    if ((mutableState.value as SearchState.Empty).data?.searchResult != null) { mutableState.update { SearchState.Content(data = updatedData) }
                    }
                }

    }
}

sealed class SearchState {
    data class Loading(val data: SearchScreenData?) : SearchState()
    data class Empty(val data: SearchScreenData?) : SearchState()
    data class Content(val data: SearchScreenData?) : SearchState()
}

data class SearchScreenData(
    val searchResult: List<CardViewData?>? = null,
    val tvGenreList: List<Genre?>? = null,
    val movieGenreList: List<Genre?>? = null
)


