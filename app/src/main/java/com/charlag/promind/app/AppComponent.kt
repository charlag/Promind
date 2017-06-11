package com.charlag.promind.app

import android.content.Context
import com.charlag.promind.core.Model
import com.charlag.promind.core.ModelModule
import com.charlag.promind.core.action.ActionHandler
import com.charlag.promind.core.action.ActionHandlerModule
import com.charlag.promind.core.context_data.ContextDataModule
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.context_data.LocationProvider
import com.charlag.promind.core.data.source.ConditionDAO
import com.charlag.promind.core.data.source.ConditionDAOModule
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.core.repository.HintsRepositoryModule
import com.charlag.promind.ui.component.hints_screen.HintsScreenModule

/**
 * Created by charlag on 25/03/2017.
 */

@javax.inject.Singleton
@dagger.Component(modules = arrayOf(AppModule::class, ModelModule::class, ConditionDAOModule::class,
        HintsScreenModule::class, ContextDataModule::class, ActionHandlerModule::class,
        HintsRepositoryModule::class))
interface AppComponent {
    fun appContext(): Context
    fun model(): Model
    fun dateProvider(): DateProvider
    fun locationProvider(): LocationProvider
    fun conditionRespository(): ConditionDAO
    fun actionHandler(): ActionHandler
    fun hintsRepository(): HintsRepository
}