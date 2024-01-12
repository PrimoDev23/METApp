package com.example.metapp.domain.usecases.interfaces

import com.example.metapp.domain.models.DetailData

interface GetDetailByIdUseCase {
    suspend operator fun invoke(id: Int): Result<DetailData>
}