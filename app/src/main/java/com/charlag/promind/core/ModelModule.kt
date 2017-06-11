package com.charlag.promind.core

import android.content.Context
import com.charlag.promind.core.builtin.weather.WeatherHintsModule
import com.charlag.promind.core.builtin.weather.WeatherHintsProvider
import com.charlag.promind.core.data.source.ConditionDAO
import com.charlag.promind.core.stats.UsageStatsSource
import com.charlag.promind.core.stats.UsageStatsSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by charlag on 25/03/2017.
 */

@Module
class ModelModule {
    @Provides
    @Singleton
    fun providesModel(repository: ConditionDAO, usageStatsSource: UsageStatsSource,
                      weatherHintsProvider: WeatherHintsProvider): Model =
            ModelImpl(repository, usageStatsSource, weatherHintsProvider)

}