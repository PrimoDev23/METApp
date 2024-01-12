package com.example.metapp.ui.viewmodels

import app.cash.turbine.test
import com.example.metapp.BaseCoroutineTest
import com.example.metapp.domain.models.SearchResult
import com.example.metapp.domain.usecases.interfaces.SearchUseCase
import com.example.metapp.sampleData.SearchResultSamples
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchScreenViewModelTest : BaseCoroutineTest() {

    @Test
    fun init() = runTest {
        val viewModel = SearchScreenViewModel(
            searchUseCase = mockk()
        )

        viewModel.state.test {
            val state = awaitItem()

            assertEquals(SearchScreenContentType.EMPTY, state.contentType)
            assertTrue(state.searchTerm.isEmpty())
            assertEquals(
                SearchResult(ids = emptyList()),
                state.searchResult
            )
        }
    }

    @Test
    fun `onSearchTermChanged - has entries`() = runTest {
        val searchUseCase = mockk<SearchUseCase>()

        val searchResult = SearchResultSamples.filled

        coEvery { searchUseCase(any()) } returns searchResult

        val viewModel = SearchScreenViewModel(
            searchUseCase = searchUseCase
        )

        viewModel.state.test {
            skipItems(1)

            val searchTerm = "Test"
            viewModel.onSearchTermChanged(searchTerm)

            var state = awaitItem()

            assertEquals(searchTerm, state.searchTerm)
            assertEquals(SearchScreenContentType.LOADING, state.contentType)

            state = awaitItem()

            assertEquals(searchResult, state.searchResult)
            assertEquals(SearchScreenContentType.RESULTS, state.contentType)

            coVerify { searchUseCase(searchTerm) }
        }
    }

    @Test
    fun `onSearchTermChanged - no entries`() = runTest {
        val searchUseCase = mockk<SearchUseCase>()

        val searchResult = SearchResultSamples.empty

        coEvery { searchUseCase(any()) } returns searchResult

        val viewModel = SearchScreenViewModel(
            searchUseCase = searchUseCase
        )

        viewModel.state.test {
            skipItems(1)

            val searchTerm = "Test"
            viewModel.onSearchTermChanged(searchTerm)

            var state = awaitItem()

            assertEquals(searchTerm, state.searchTerm)
            assertEquals(SearchScreenContentType.LOADING, state.contentType)

            state = awaitItem()

            assertEquals(searchResult, state.searchResult)
            assertEquals(SearchScreenContentType.EMPTY, state.contentType)

            coVerify { searchUseCase(searchTerm) }
        }
    }

    @Test
    fun `onSearchTermChanged - clear term`() = runTest {
        val searchUseCase = mockk<SearchUseCase>()

        val searchResult = SearchResultSamples.filled

        coEvery { searchUseCase(any()) } returns searchResult

        val viewModel = SearchScreenViewModel(
            searchUseCase = searchUseCase
        )

        viewModel.state.test {
            viewModel.onSearchTermChanged("Test")

            skipItems(3)

            val emptyTerm = ""
            viewModel.onSearchTermChanged(emptyTerm)

            val state = awaitItem()

            assertEquals(emptyTerm, state.searchTerm)
            assertEquals(SearchScreenContentType.EMPTY, state.contentType)
            assertTrue(state.searchResult.ids.isEmpty())

            coVerify(inverse = true) { searchUseCase(emptyTerm) }
        }
    }

}