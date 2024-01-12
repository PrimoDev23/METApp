package com.example.metapp.data.remotes

import com.example.metapp.data.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRemote {

    @GET("search")
    suspend fun search(@Query("q") query: String): Result<SearchResponse>

}