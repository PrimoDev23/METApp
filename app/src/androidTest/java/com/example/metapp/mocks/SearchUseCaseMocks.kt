package com.example.metapp.mocks

import com.example.metapp.domain.models.SearchResult
import com.example.metapp.domain.usecases.interfaces.SearchUseCase

class SearchUseCaseSuccessMock(
    private val result: SearchResult
) : SearchUseCase {
    override suspend fun invoke(term: String): Result<SearchResult> {
        return Result.success(result)
    }
}

class SearchUseCaseFailureMock : SearchUseCase {
    override suspend fun invoke(term: String): Result<SearchResult> {
        return Result.failure(Exception())
    }
}