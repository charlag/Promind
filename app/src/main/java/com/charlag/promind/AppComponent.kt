package com.charlag.promind

import android.app.Application
import com.charlag.promind.core.ModelModule
import com.charlag.promind.core.data.source.ConditionRepositoryModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by charlag on 25/03/2017.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, ModelModule::class, ConditionRepositoryModule::class))
interface AppComponent {
    fun inject(application: Application)

    fun inject(mainActivity: MainActivity)
}