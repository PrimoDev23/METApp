package com.example.metapp.domain.usecases

import com.example.metapp.domain.models.SearchResult
import com.example.metapp.domain.repositories.SearchRepository
import com.example.metapp.domain.usecases.interfaces.SearchUseCase

class SearchUseCaseImpl(
    private val searchRepo: SearchRepository
): SearchUseCase {
    override suspend fun invoke(term: String): Result<SearchResult> {
        return searchRepo.search(term)
    }
}