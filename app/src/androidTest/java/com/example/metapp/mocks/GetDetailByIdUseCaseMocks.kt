package com.example.metapp.mocks

import com.example.metapp.domain.models.DetailData
import com.example.metapp.domain.usecases.interfaces.GetDetailByIdUseCase

// Mockk throws when returning a Result, so we need a manual mock for these

class GetDetailByIdUseCaseSuccessMock(
    private val data: DetailData
) : GetDetailByIdUseCase {
    override suspend fun invoke(id: Int): Result<DetailData> {
        return Result.success(data)
    }
}

class GetDetailByIdUseCaseErrorMock : GetDetailByIdUseCase {
    override suspend fun invoke(id: Int): Result<DetailData> {
        return Result.failure(Exception())
    }
}