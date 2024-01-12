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
        val data = getDetailByIdUseCase(navArgs.id)

        _state.update {
            it.copy(data = data)
        }
    }

}

data class DetailScreenState(
    val data: DetailData? = null
)