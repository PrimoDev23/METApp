package com.example.metapp

import com.example.metapp.data.remotes.SearchRemote
import com.example.metapp.data.repositories.SearchRepositoryImpl
import com.example.metapp.domain.repositories.SearchRepository
import com.example.metapp.domain.usecases.SearchUseCaseImpl
import com.example.metapp.domain.usecases.interfaces.SearchUseCase
import com.example.metapp.domain.usecases.interfaces.SearchUseCaseImpl
import com.example.metapp.ui.viewmodels.SearchScreenViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val viewModelModule = module {
    viewModelOf(::SearchScreenViewModel)
}

val remoteModule = module {
    factory {
        Retrofit.Builder()
            .baseUrl("https://collectionapi.metmuseum.org/public/collection/v1/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
    }
    factory {
        get<Retrofit>().create<SearchRemote>()
    }
}

val repositoryModule = module {
    factoryOf(::SearchRepositoryImpl) bind SearchRepository::class
}

val useCaseModule = module {
    factoryOf(::SearchUseCaseImpl) bind SearchUseCase::class
}