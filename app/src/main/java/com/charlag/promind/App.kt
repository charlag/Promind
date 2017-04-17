package com.charlag.promind

import android.app.Application
import com.charlag.promind.core.ModelModule
import com.charlag.promind.core.context_data.ContextDataModule
import com.charlag.promind.core.data.source.ConditionRepositoryModule

/**
 * Created by charlag on 25/03/2017.
 */

class App : Application() {

    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        graph = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .conditionRepositoryModule(ConditionRepositoryModule())
                .contextDataModule(ContextDataModule())
                .modelModule(ModelModule())
                .appDataSourceModule(AppDataSourceModule())
                .build()
    }
}