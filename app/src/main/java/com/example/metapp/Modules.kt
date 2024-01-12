package com.example.metapp

import com.example.metapp.data.remotes.DetailRemote
import com.example.metapp.data.remotes.SearchRemote
import com.example.metapp.data.repositories.DetailRepositoryImpl
import com.example.metapp.data.repositories.SearchRepositoryImpl
import com.example.metapp.domain.repositories.DetailRepository
import com.example.metapp.domain.repositories.SearchRepository
import com.example.metapp.domain.usecases.GetDetailByIdUseCaseImpl
import com.example.metapp.domain.usecases.SearchUseCaseImpl
import com.example.metapp.domain.usecases.interfaces.GetDetailByIdUseCase
import com.example.metapp.domain.usecases.interfaces.SearchUseCase
import com.example.metapp.ui.viewmodels.DetailScreenViewModel
import com.example.metapp.ui.viewmodels.SearchScreenViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val viewModelModule = module {
    viewModelOf(::SearchScreenViewModel)
    viewModelOf(::DetailScreenViewModel)
}

val remoteModule = module {
    factory {
        Json {
            ignoreUnknownKeys = true
        }
    }
    factory {
        val json = get<Json>()

        Retrofit.Builder()
            .baseUrl("https://collectionapi.metmuseum.org/public/collection/v1/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    factory {
        get<Retrofit>().create<SearchRemote>()
    }
    factory {
        get<Retrofit>().create<DetailRemote>()
    }
}

val repositoryModule = module {
    factoryOf(::SearchRepositoryImpl) bind SearchRepository::class
    factoryOf(::DetailRepositoryImpl) bind DetailRepository::class
}

val useCaseModule = module {
    factoryOf(::SearchUseCaseImpl) bind SearchUseCase::class
    factoryOf(::GetDetailByIdUseCaseImpl) bind GetDetailByIdUseCase::class
}