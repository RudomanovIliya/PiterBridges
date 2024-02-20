package com.example.piterbridges.data.remote.repository

import com.example.piterbridges.presentation.bridges.model.Bridge

interface BridgesRepository {

    /** Получить мосты */
    suspend fun getBridges(): List<Bridge>

    /** Получить мост */
    suspend fun getBridge(id: Int): Bridge
}