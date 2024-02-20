package com.example.piterbridges.presentation

import com.example.piterbridges.R
import com.example.piterbridges.di.DaggerApplicationComponent
import com.yandex.mapkit.MapKitFactory
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class BridgesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(getString(R.string.yandex_key))
        MapKitFactory.initialize(this)
    }
}