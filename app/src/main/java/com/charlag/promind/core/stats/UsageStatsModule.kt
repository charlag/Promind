package com.charlag.promind.core.stats

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by charlag on 11/06/2017.
 */

@Module
class UsageStatsModule {
    @Provides
    @Singleton
    fun providesUsageStatsSource(context: Context): UsageStatsSource = UsageStatsSourceImpl(context)
}