package com.charlag.promind

import android.content.Context
import com.charlag.promind.core.Model
import com.charlag.promind.core.ModelModule
import com.charlag.promind.core.context_data.ContextDataModule
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.data.source.ConditionRepository
import com.charlag.promind.core.data.source.ConditionRepositoryModule
import com.charlag.promind.hints_screen.HintsScreenModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by charlag on 25/03/2017.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, ModelModule::class, ConditionRepositoryModule::class,
        HintsScreenModule::class, ContextDataModule::class, AppDataSourceModule::class))
interface AppComponent {
    fun appContext(): Context
    fun model(): Model
    fun dateProvider(): DateProvider
    fun locationProvider(): LocationProvider
    fun conditionRespository(): ConditionRepository
    fun appDataSource(): AppDataSource
}