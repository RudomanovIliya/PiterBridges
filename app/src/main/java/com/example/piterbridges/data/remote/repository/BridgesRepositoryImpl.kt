package com.example.piterbridges.data.remote.repository


import com.example.piterbridges.data.MainApiService
import com.example.piterbridges.data.remote.model.toModel
import com.example.piterbridges.presentation.bridges.model.Bridge
import javax.inject.Inject

class BridgesRepositoryImpl @Inject constructor(
    private val apiService: MainApiService,
) : BridgesRepository {

    override suspend fun getBridges(): List<Bridge> {
        return apiService.getBridges().map { it.toModel() }
    }

    override suspend fun getBridge(id: Int): Bridge {
        return apiService.getBridge(id).toModel()
    }
}