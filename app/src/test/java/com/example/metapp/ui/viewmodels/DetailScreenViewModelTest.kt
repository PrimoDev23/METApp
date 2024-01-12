package com.example.metapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.metapp.BaseCoroutineTest
import com.example.metapp.domain.usecases.interfaces.GetDetailByIdUseCase
import com.example.metapp.sampleData.DetailDataSamples
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailScreenViewModelTest : BaseCoroutineTest() {

    private fun buildSavedStateHandle(id: Int) = SavedStateHandle().apply {
        this["id"] = id
    }

    @Test
    fun init() = runTest {
        val id = 0
        val savedStateHandle = buildSavedStateHandle(id)
        val getDetailByIdUseCase = mockk<GetDetailByIdUseCase>()

        val detailData = DetailDataSamples.detailData

        coEvery { getDetailByIdUseCase(id) } returns detailData

        val viewModel = DetailScreenViewModel(
            savedStateHandle = savedStateHandle,
            getDetailByIdUseCase = getDetailByIdUseCase
        )

        viewModel.state.test {
            var state = awaitItem()

            assertEquals(null, state.data)

            state = awaitItem()

            assertEquals(detailData, state.data)

            coVerify { getDetailByIdUseCase(id) }
        }
    }

}