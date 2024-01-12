package com.example.metapp.domain.repositories

import com.example.metapp.domain.models.SearchResult

interface SearchRepository {
    suspend fun search(term: String): Result<SearchResult>
}