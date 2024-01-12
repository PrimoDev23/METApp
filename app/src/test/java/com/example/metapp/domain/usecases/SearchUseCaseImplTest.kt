package com.example.metapp.domain.usecases

import com.example.metapp.BaseCoroutineTest
import com.example.metapp.domain.repositories.SearchRepository
import com.example.metapp.sampleData.SearchResultSamples
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchUseCaseImplTest : BaseCoroutineTest() {

    @Test
    operator fun invoke() = runTest {
        val repo = mockk<SearchRepository>()

        val term = "Test"
        val searchResult = SearchResultSamples.filled
        coEvery { repo.search(term) } returns Result.success(searchResult)

        val useCase = SearchUseCaseImpl(
            searchRepo = repo
        )

        val result = useCase(term)

        assertEquals(Result.success(searchResult), result)

        coVerify { repo.search(term) }
    }
}