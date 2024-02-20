package com.example.piterbridges.di

import com.example.piterbridges.presentation.bridges.InfoBridgeFragment
import com.example.piterbridges.presentation.bridges.ListBridgeFragment
import com.example.piterbridges.presentation.map.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun listBridgeFragment(): ListBridgeFragment

    @ContributesAndroidInjector
    abstract fun infoBridgeFragment(): InfoBridgeFragment

    @ContributesAndroidInjector
    abstract fun MapFragment(): MapFragment
}