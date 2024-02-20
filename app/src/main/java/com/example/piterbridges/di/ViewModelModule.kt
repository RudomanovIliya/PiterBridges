package com.example.piterbridges.di

import androidx.lifecycle.ViewModel
import com.example.piterbridges.presentation.bridges.InfoBridgeViewModel
import com.example.piterbridges.presentation.bridges.ListBridgesViewModel
import com.example.piterbridges.presentation.map.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListBridgesViewModel::class)
    abstract fun listBridgesViewModel(viewModel: ListBridgesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfoBridgeViewModel::class)
    abstract fun infoBridgeViewModel(viewModel: InfoBridgeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun mapViewModel(viewModel: MapViewModel): ViewModel
}