package com.example.piterbridges.data

import com.example.piterbridges.data.remote.model.ApiBridge
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApiService {
    @GET("bridges")
    suspend fun getBridges(): List<ApiBridge>

    @GET("bridges/{id}")
    suspend fun getBridge(@Path("id") id: Int): ApiBridge
}