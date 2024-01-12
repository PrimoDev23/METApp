package com.example.metapp.data.remotes

import com.example.metapp.data.models.DetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailRemote {

    @GET("objects/{id}")
    suspend fun getDetailById(@Path("id") id: Int): DetailResponse

}