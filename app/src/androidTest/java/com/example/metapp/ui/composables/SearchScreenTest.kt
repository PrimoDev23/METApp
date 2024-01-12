package com.example.metapp.ui.composables

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.metapp.BaseAndroidComposeTest
import com.example.metapp.R
import com.example.metapp.domain.models.SearchResult
import com.example.metapp.mocks.SearchUseCaseFailureMock
import com.example.metapp.mocks.SearchUseCaseSuccessMock
import com.example.metapp.ui.composables.destinations.DetailScreenDestination
import com.example.metapp.ui.viewmodels.SearchScreenViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchScreenTest : BaseAndroidComposeTest() {

    @Test
    fun initialState() {
        val viewModel = SearchScreenViewModel(
            searchUseCase = mockk()
        )

        composeTestRule.setContent {
            SearchScreen(
                navigator = mockk(),
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.search_screen_empty_state))
            .assertExists()
    }

    @Test
    fun searchHasItems() {
        val searchResult = SearchResult(ids = listOf(0, 1, 2, 3))
        val useCase = SearchUseCaseSuccessMock(result = searchResult)

        val viewModel = SearchScreenViewModel(
            searchUseCase = useCase
        )

        composeTestRule.setContent {
            SearchScreen(
                navigator = mockk(),
                viewModel = viewModel
            )
        }

        val searchField =
            composeTestRule.onNodeWithText(context.getString(R.string.search_screen_search_placeholder))

        searchField.performTextInput("Test")

        searchResult.ids.forEach {
            composeTestRule.onNodeWithText(it.toString()).assertExists()
        }
    }

    @Test
    fun searchEmpty() {
        val searchResult = SearchResult(ids = emptyList())
        val useCase = SearchUseCaseSuccessMock(result = searchResult)

        val viewModel = SearchScreenViewModel(
            searchUseCase = useCase
        )

        composeTestRule.setContent {
            SearchScreen(
                navigator = mockk(),
                viewModel = viewModel
            )
        }

        val searchField =
            composeTestRule.onNodeWithText(context.getString(R.string.search_screen_search_placeholder))

        searchField.performTextInput("Test")

        composeTestRule.onNodeWithText(context.getString(R.string.search_screen_empty_state))
            .assertExists()
    }

    @Test
    fun searchError() {
        val useCase = SearchUseCaseFailureMock()

        val viewModel = SearchScreenViewModel(
            searchUseCase = useCase
        )

        composeTestRule.setContent {
            SearchScreen(
                navigator = mockk(),
                viewModel = viewModel
            )
        }

        val searchField =
            composeTestRule.onNodeWithText(context.getString(R.string.search_screen_search_placeholder))
        val searchTerm = "Test"

        searchField.performTextInput(searchTerm)

        composeTestRule.onNodeWithText(searchTerm).assertExists()

        composeTestRule.onNodeWithText(context.getString(R.string.search_screen_error_state))
            .assertExists()
    }

    @Test
    fun navigateToDetail() {
        val searchResult = SearchResult(ids = listOf(0, 1, 2, 3))
        val useCase = SearchUseCaseSuccessMock(result = searchResult)

        val navigator = mockk<DestinationsNavigator>()

        every { navigator.navigate(any<Direction>()) } returns Unit

        val viewModel = SearchScreenViewModel(
            searchUseCase = useCase
        )

        composeTestRule.setContent {
            SearchScreen(
                navigator = navigator,
                viewModel = viewModel
            )
        }

        val searchField =
            composeTestRule.onNodeWithText(context.getString(R.string.search_screen_search_placeholder))

        searchField.performTextInput("Test")

        searchResult.ids.forEach {
            composeTestRule.onNodeWithText(it.toString()).performClick()

            verify { navigator.navigate(DetailScreenDestination(id = it)) }
        }
    }

    @Test
    fun clearSearch() {
        val searchResult = SearchResult(ids = listOf(0, 1, 2, 3))
        val useCase = SearchUseCaseSuccessMock(result = searchResult)

        val viewModel = SearchScreenViewModel(
            searchUseCase = useCase
        )

        composeTestRule.setContent {
            SearchScreen(
                navigator = mockk(),
                viewModel = viewModel
            )
        }

        val searchField =
            composeTestRule.onNodeWithText(context.getString(R.string.search_screen_search_placeholder))

        searchField.performTextInput("Test")

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.general_clear))
            .performClick()

        assertTrue(searchField.fetchSemanticsNode().config[SemanticsProperties.EditableText].text.isEmpty())
    }

}