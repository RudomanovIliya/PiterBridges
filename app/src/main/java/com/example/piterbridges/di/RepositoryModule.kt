package com.example.piterbridges.di

import com.example.piterbridges.data.remote.repository.BridgesRepository
import com.example.piterbridges.data.remote.repository.BridgesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideBridgesRepository(
        bridgesRepository: BridgesRepositoryImpl,
    ): BridgesRepository
}