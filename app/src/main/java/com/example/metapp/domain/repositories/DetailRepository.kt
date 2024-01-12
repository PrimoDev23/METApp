package com.example.metapp.domain.repositories

import com.example.metapp.domain.models.DetailData

interface DetailRepository {
    suspend fun getDetailById(id: Int): DetailData
}