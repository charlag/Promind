package com.charlag.promind.app

import android.app.Application
import com.charlag.promind.core.ModelModule
import com.charlag.promind.core.builtin.weather.WeatherHintsModule
import com.charlag.promind.core.context_data.ContextDataModule
import com.charlag.promind.core.data.source.ConditionDAOModule
import com.charlag.promind.core.repository.HintsRepositoryModule
import com.charlag.promind.core.stats.UsageStatsModule

/**
 * Created by charlag on 25/03/2017.
 */

class App : Application() {

    lateinit var graph: AppComponent

    override fun onCreate() {
        super.onCreate()

        graph = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .conditionDAOModule(ConditionDAOModule())
                .contextDataModule(ContextDataModule())
                .modelModule(ModelModule())
                .usageStatsModule(UsageStatsModule())
                .hintsRepositoryModule(HintsRepositoryModule())
                .weatherHintsModule(WeatherHintsModule())
                .build()
    }
}