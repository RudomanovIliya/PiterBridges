package com.example.piterbridges.presentation.bridges

import com.example.piterbridges.presentation.bridges.model.Bridge

fun interface BridgeListener {
    fun onBridgeClick(bridge: Bridge)
}