package com.example.metapp.data.repositories

import com.example.metapp.BaseCoroutineTest
import com.example.metapp.data.remotes.DetailRemote
import com.example.metapp.sampleData.DetailDataSamples
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailRepositoryImplTest : BaseCoroutineTest() {

    @Test
    fun getDetailById() = runTest {
        val remote = mockk<DetailRemote>()

        val id = 0
        val detailResponse = DetailDataSamples.detailResponse

        coEvery { remote.getDetailById(id) } returns Result.success(detailResponse)

        val repo = DetailRepositoryImpl(
            detailRemote = remote
        )

        val result = repo.getDetailById(id)

        assertEquals(Result.success(DetailDataSamples.detailData), result)

        coVerify { remote.getDetailById(id) }
    }
}