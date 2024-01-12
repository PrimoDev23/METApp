package com.example.metapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metapp.domain.models.DetailData
import com.example.metapp.domain.usecases.interfaces.GetDetailByIdUseCase
import com.example.metapp.ui.composables.navArgs
import com.example.metapp.ui.navigation.arguments.DetailNavArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val getDetailByIdUseCase: GetDetailByIdUseCase
) : ViewModel() {

    private val navArgs: DetailNavArgs = savedStateHandle.navArgs()

    private val _state = MutableStateFlow(DetailScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            initData()
        }
    }

    private suspend fun initData() {
        val result = getDetailByIdUseCase(navArgs.id)

        _state.update {
            val data = result.getOrElse { _ ->
                return@update it.copy(
                    contentType = DetailScreenContentType.ERROR,
                    data = null
                )
            }

            return@update it.copy(
                contentType = DetailScreenContentType.RESULT,
                data = data
            )
        }
    }

}

data class DetailScreenState(
    val contentType: DetailScreenContentType = DetailScreenContentType.LOADING,
    val data: DetailData? = null
)

enum class DetailScreenContentType {
    LOADING,
    ERROR,
    RESULT
}