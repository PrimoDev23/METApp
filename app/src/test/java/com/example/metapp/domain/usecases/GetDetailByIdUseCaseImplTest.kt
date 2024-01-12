package com.example.metapp.domain.usecases

import com.example.metapp.BaseCoroutineTest
import com.example.metapp.domain.repositories.DetailRepository
import com.example.metapp.sampleData.DetailDataSamples
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDetailByIdUseCaseImplTest : BaseCoroutineTest() {

    @Test
    operator fun invoke() = runTest {
        val repo = mockk<DetailRepository>()

        val id = 0
        val detailData = DetailDataSamples.detailData
        coEvery { repo.getDetailById(id) } returns detailData

        val useCase = GetDetailByIdUseCaseImpl(
            detailRepository = repo
        )

        val result = useCase(id)

        assertEquals(result, detailData)

        coVerify { repo.getDetailById(id) }
    }
}