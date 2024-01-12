package com.example.metapp.domain.usecases.interfaces

import com.example.metapp.domain.models.SearchResult

interface SearchUseCase {
    suspend operator fun invoke(term: String): Result<SearchResult>
}