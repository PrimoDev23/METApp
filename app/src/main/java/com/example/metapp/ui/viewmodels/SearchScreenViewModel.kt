package com.example.metapp.ui.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metapp.domain.models.SearchResult
import com.example.metapp.domain.usecases.interfaces.SearchUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private var searchJob: Job? = null

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    fun onSearchTermChanged(term: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (term.isBlank()) {
                _state.update {
                    it.copy(
                        searchTerm = term,
                        contentType = SearchScreenContentType.EMPTY,
                        searchResult = SearchResult(ids = emptyList())
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    searchTerm = term,
                    contentType = SearchScreenContentType.LOADING
                )
            }

            val result = searchUseCase(term)
                .getOrElse {
                    _state.update {
                        it.copy(
                            searchResult = SearchResult(ids = emptyList()),
                            contentType = SearchScreenContentType.ERROR
                        )
                    }

                    return@launch
                }

            _state.update {
                it.copy(
                    searchResult = result,
                    contentType = if (result.ids.isNotEmpty()) {
                        SearchScreenContentType.RESULTS
                    } else {
                        SearchScreenContentType.EMPTY
                    }
                )
            }
        }
    }

}

@Immutable
data class SearchScreenState(
    val searchTerm: String = "",
    val contentType: SearchScreenContentType = SearchScreenContentType.EMPTY,
    val searchResult: SearchResult = SearchResult(ids = emptyList())
)

enum class SearchScreenContentType {
    LOADING,
    ERROR,
    EMPTY,
    RESULTS
}