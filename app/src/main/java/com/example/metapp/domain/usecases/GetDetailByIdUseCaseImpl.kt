package com.example.metapp.domain.usecases

import com.example.metapp.domain.models.DetailData
import com.example.metapp.domain.repositories.DetailRepository
import com.example.metapp.domain.usecases.interfaces.GetDetailByIdUseCase

class GetDetailByIdUseCaseImpl(
    private val detailRepository: DetailRepository
) : GetDetailByIdUseCase {
    override suspend fun invoke(id: Int): DetailData {
        return detailRepository.getDetailById(id)
    }
}