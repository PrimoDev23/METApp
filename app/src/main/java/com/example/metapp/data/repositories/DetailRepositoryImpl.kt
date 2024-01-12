package com.example.metapp.data.repositories

import com.example.metapp.data.remotes.DetailRemote
import com.example.metapp.domain.models.DetailData
import com.example.metapp.domain.repositories.DetailRepository

class DetailRepositoryImpl(
    private val detailRemote: DetailRemote
) : DetailRepository {
    override suspend fun getDetailById(id: Int): Result<DetailData> {
        val result = detailRemote.getDetailById(id)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(result.toDomainModel())
    }
}