package com.charlag.promind.core.app_data

/**
 * Created by charlag on 17/04/2017.
 */

@dagger.Module
class AppDataProviderModule {
    @javax.inject.Singleton
    @dagger.Provides
    fun provideAppDataSource(context: android.content.Context): AppDataProvider
            = AppDataProviderImpl(context)
}