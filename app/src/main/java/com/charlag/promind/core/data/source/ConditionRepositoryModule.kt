package com.charlag.promind.core.data.source

import android.content.Context
import com.charlag.promind.core.data.source.db.ConditionDbHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by charlag on 25/03/2017.
 */

@Module
class ConditionRepositoryModule {
    @Provides
    @Singleton
    fun providesConditionRepository(context: Context): ConditionRepository =
            ConditionDbRepository(ConditionDbHelper(context))
}