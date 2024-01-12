package com.example.metapp.data.repositories

import com.example.metapp.data.remotes.SearchRemote
import com.example.metapp.domain.models.SearchResult
import com.example.metapp.domain.repositories.SearchRepository

class SearchRepositoryImpl(
    private val searchRemote: SearchRemote
) : SearchRepository {
    override suspend fun search(term: String): Result<SearchResult> {
        val result = searchRemote.search(term)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(result.toDomainModel())
    }
}