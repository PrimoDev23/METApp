package com.example.metapp.ui.composables

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.test.FakeImageLoaderEngine
import com.example.metapp.BaseAndroidComposeTest
import com.example.metapp.R
import com.example.metapp.domain.models.DetailData
import com.example.metapp.mocks.GetDetailByIdUseCaseErrorMock
import com.example.metapp.mocks.GetDetailByIdUseCaseSuccessMock
import com.example.metapp.ui.viewmodels.DetailScreenViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class DetailScreenTest : BaseAndroidComposeTest() {

    private val testDrawable = ColorDrawable(Color.DKGRAY)

    @OptIn(ExperimentalCoilApi::class)
    @Before
    fun before() {
        val engine = FakeImageLoaderEngine(testDrawable)
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(engine)
            }
            .build()

        Coil.setImageLoader(imageLoader)
    }

    private fun buildSavedStateHandle(id: Int) = SavedStateHandle().apply {
        this["id"] = 0
    }

    @Test
    fun loadDetailMandatoryEmpty() {
        val id = 0

        val navigator = mockk<DestinationsNavigator>()

        val isHighlightText = context.getText(R.string.general_yes)
        val objectUrlText = context.getText(R.string.detail_screen_website)

        val data = DetailData(
            primaryImage = "",
            additionalImages = emptyList(),
            isHighlight = true,
            title = "Mona Lisa",
            department = "Art",
            objectUrl = ""
        )
        val getDetailByIdUseCase = GetDetailByIdUseCaseSuccessMock(data)

        val viewModel = DetailScreenViewModel(
            savedStateHandle = buildSavedStateHandle(id),
            getDetailByIdUseCase = getDetailByIdUseCase
        )

        composeTestRule.setContent {
            DetailScreen(
                navigator = navigator,
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("PrimaryImage").assertDoesNotExist()
        composeTestRule.onNodeWithTag("AdditionalImages").assertDoesNotExist()
        composeTestRule.onNodeWithText(data.title).assertExists()
        composeTestRule.onNodeWithText(data.department).assertExists()
        composeTestRule.onNodeWithText(isHighlightText.toString()).assertExists()
        composeTestRule.onNodeWithText(objectUrlText.toString()).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loadDetailMandatoryFilled() {
        val id = 0

        val navigator = mockk<DestinationsNavigator>()

        val isHighlightText = context.getText(R.string.general_no)

        val data = DetailData(
            primaryImage = TEST_URL,
            additionalImages = listOf(
                TEST_URL
            ),
            isHighlight = false,
            title = "Mona Lisa",
            department = "Art",
            objectUrl = TEST_URL
        )
        val getDetailByIdUseCase = GetDetailByIdUseCaseSuccessMock(data)

        val viewModel = DetailScreenViewModel(
            savedStateHandle = buildSavedStateHandle(id),
            getDetailByIdUseCase = getDetailByIdUseCase
        )

        composeTestRule.setContent {
            DetailScreen(
                navigator = navigator,
                viewModel = viewModel
            )
        }

        composeTestRule.waitUntilNodeCount(
            matcher = hasTestTag("PrimaryImage"),
            count = 1
        )

        composeTestRule.onNodeWithTag("PrimaryImage").assertExists()
        composeTestRule.onNodeWithTag("AdditionalImages").assertExists()
        composeTestRule.onNodeWithText(data.title).assertExists()
        composeTestRule.onNodeWithText(data.department).assertExists()
        composeTestRule.onNodeWithText(isHighlightText.toString()).assertExists()
        composeTestRule.onNodeWithText(data.objectUrl).assertExists()
    }

    @Test
    fun loadDetailError() {
        val id = 0

        val navigator = mockk<DestinationsNavigator>()
        val getDetailByIdUseCase = GetDetailByIdUseCaseErrorMock()

        val viewModel = DetailScreenViewModel(
            savedStateHandle = buildSavedStateHandle(id),
            getDetailByIdUseCase = getDetailByIdUseCase
        )

        composeTestRule.setContent {
            DetailScreen(
                navigator = navigator,
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.detail_screen_error_state))
    }

    companion object {
        private const val TEST_URL = "https://google.com"
    }
}