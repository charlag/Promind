package com.charlag.promind.core.model

import com.charlag.promind.core.builtin.weather.WeatherHintsProvider
import com.charlag.promind.core.data.source.ConditionDAO
import com.charlag.promind.core.stats.UsageStatsSource

/**
 * Created by charlag on 25/03/2017.
 */

@dagger.Module
class ModelModule {
    @dagger.Provides
    @javax.inject.Singleton
    fun providesModel(repository: ConditionDAO, usageStatsSource: UsageStatsSource,
                      weatherHintsProvider: WeatherHintsProvider): Model =
            ModelImpl(repository, usageStatsSource, weatherHintsProvider)

}