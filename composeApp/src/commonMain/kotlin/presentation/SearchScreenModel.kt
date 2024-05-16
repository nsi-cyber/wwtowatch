package presentation

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.model.searchResultList.SearchResultItem
import data.repository.ImdbRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SearchScreenModel(
    private val imdbRepository: ImdbRepository
) :
    StateScreenModel<ListState>(ListState.Loading) {

    private var isLoadingSearch = mutableStateOf(false)
    fun getTopRatedShows(query:String) = screenModelScope.launch {
        if (isLoadingSearch.value) return@launch
        isLoadingSearch.value = true

        val result = imdbRepository.getSearchResults(query,1)

        result.onSuccess { searchResult ->
            if (searchResult.isNullOrEmpty()) {
                mutableState.update { ListState.Empty }
            } else {

                    mutableState.update { SearchState.Content(searchResult =searchResult.filter { !it?.media_type.isNullOrEmpty() }) }

            }
        }.onFailure { t ->
            val errorMessage = t.message.orEmpty()
            println("Error fetching movies: $errorMessage") // Log the error
            mutableState.update { ListState.ShowError(errorMessage) }
        }

        isLoadingSearch.value = false // Yüklenme tamamlandı
    }


}

sealed interface SearchState {
    data object Loading : ListState
    data object Empty : ListState
    data class Content(
        val searchResult: List<SearchResultItem?>? = null,
    ) : ListState

    data class ShowError(val message: String?) : ListState
}

