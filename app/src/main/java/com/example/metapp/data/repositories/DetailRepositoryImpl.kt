package com.example.metapp.data.repositories

import com.example.metapp.data.remotes.DetailRemote
import com.example.metapp.domain.models.DetailData
import com.example.metapp.domain.repositories.DetailRepository

class DetailRepositoryImpl(
    private val detailRemote: DetailRemote
) : DetailRepository {
    override suspend fun getDetailById(id: Int): DetailData {
        return detailRemote.getDetailById(id).toDomainModel()
    }
}