package com.example.metapp.data.repositories

import com.example.metapp.data.remotes.SearchRemote
import com.example.metapp.domain.models.SearchResult
import com.example.metapp.domain.repositories.SearchRepository

class SearchRepositoryImpl(
    private val searchRemote: SearchRemote
): SearchRepository {
    override suspend fun search(term: String): SearchResult {
        return searchRemote.search(term).toDomainModel()
    }
}