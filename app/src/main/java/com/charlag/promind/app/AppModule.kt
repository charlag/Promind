package com.charlag.promind.app

import android.content.Context
import android.location.LocationManager

/**
 * Created by charlag on 25/03/2017.
 */

@dagger.Module
class AppModule(private val application: App) {
    @dagger.Provides
    // TODO: add qualifier
    @javax.inject.Singleton
    fun providesApplicationContext(): Context = application

    @dagger.Provides
    @javax.inject.Singleton
    fun providesLocationManager(): LocationManager =
            application.getSystemService(
                    android.content.Context.LOCATION_SERVICE) as LocationManager
}