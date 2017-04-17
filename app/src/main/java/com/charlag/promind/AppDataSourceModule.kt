package com.charlag.promind

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by charlag on 17/04/2017.
 */

@Module
class AppDataSourceModule {
    @Singleton
    @Provides
    fun provideAppDataSource(context: Context): AppDataSource = AppDataSourceImpl(context)
}