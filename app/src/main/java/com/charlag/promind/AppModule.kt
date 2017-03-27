package com.charlag.promind

import android.content.Context
import android.location.LocationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by charlag on 25/03/2017.
 */

@Module
class AppModule(private val application: App) {
    @Provides
    // TODO: add qualifier
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesLocationManager(): LocationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
}