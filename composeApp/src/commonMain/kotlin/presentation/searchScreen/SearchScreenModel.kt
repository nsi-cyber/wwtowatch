package presentation.searchScreen

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.CardViewData
import data.model.searchResultList.SearchResultItem
import data.model.searchResultList.toCardViewData
import data.repository.ImdbRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SearchScreenModel(
    private val imdbRepository: ImdbRepository
) :
    StateScreenModel<SearchState>(SearchState.Empty) {

    private var isLoadingSearch = mutableStateOf(false)

    private var searchJob: Job? = null


    fun setStateEmpty(){
        mutableState.update { SearchState.Empty }
    }
    fun getSearchResults(query:String) = screenModelScope.launch {

        isLoadingSearch.value = true

        val result = imdbRepository.getSearchResults(query,1)

        result.onSuccess { searchResult ->
            if (searchResult.isNullOrEmpty()) {
                mutableState.update { SearchState.Empty }
            } else {

                    mutableState.update { SearchState.Content(searchResult = searchResult.filter { !it?.media_type.isNullOrEmpty() &&it?.media_type!="person" }.map { it?.toCardViewData() }) }

            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            mutableState.update { SearchState.ShowError(errorMessage) }
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


}

sealed interface SearchState {
    data object Loading : SearchState
    data object Empty : SearchState
    data class Content(
        val searchResult: List<CardViewData?>? = null,
    ) : SearchState

    data class ShowError(val message: String?) : SearchState
}

