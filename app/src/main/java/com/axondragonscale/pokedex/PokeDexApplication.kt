package com.axondragonscale.pokedex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Ronak Harkhani on 25/05/21
 */
@HiltAndroidApp
class PokeDexApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Plant the Timber debug tree for logging
        Timber.plant(Timber.DebugTree())
    }
}