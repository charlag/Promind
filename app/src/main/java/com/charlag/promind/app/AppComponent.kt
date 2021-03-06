package com.charlag.promind.app

import android.content.Context
import com.charlag.promind.core.model.Model
import com.charlag.promind.core.model.ModelModule
import com.charlag.promind.core.action.ActionHandler
import com.charlag.promind.core.action.ActionHandlerModule
import com.charlag.promind.core.apps.AppsSource
import com.charlag.promind.core.apps.AppsSourceModule
import com.charlag.promind.core.builtin.weather.WeatherHintsModule
import com.charlag.promind.core.context_data.ContextDataModule
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.context_data.LocationProvider
import com.charlag.promind.core.data.source.ConditionDAO
import com.charlag.promind.core.data.source.ConditionDAOModule
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.core.repository.HintsRepositoryModule
import com.charlag.promind.core.stats.UsageStatsModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by charlag on 25/03/2017.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, ModelModule::class, ConditionDAOModule::class,
        ContextDataModule::class, ActionHandlerModule::class,
        HintsRepositoryModule::class, UsageStatsModule::class, WeatherHintsModule::class,
        AppsSourceModule::class))
interface AppComponent {
    fun appContext(): Context
    fun model(): Model
    fun dateProvider(): DateProvider
    fun locationProvider(): LocationProvider
    fun conditionRespository(): ConditionDAO
    fun actionHandler(): ActionHandler
    fun hintsRepository(): HintsRepository
    fun appsSource(): AppsSource
}