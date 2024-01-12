package com.example.metapp.data.repositories

import com.example.metapp.BaseCoroutineTest
import com.example.metapp.data.remotes.SearchRemote
import com.example.metapp.sampleData.SearchResultSamples
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchRepositoryImplTest : BaseCoroutineTest() {

    @Test
    fun search() = runTest {
        val remote = mockk<SearchRemote>()

        val term = "Test"
        val searchResult = SearchResultSamples.filledResponse

        coEvery { remote.search(term) } returns searchResult

        val repo = SearchRepositoryImpl(
            searchRemote = remote
        )

        val result = repo.search(term)

        assertEquals(SearchResultSamples.filled, result)

        coVerify { remote.search(term) }
    }
}