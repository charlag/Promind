package com.charlag.promind.core

import com.charlag.promind.core.data.source.ConditionRepository
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
    fun providesModel(repository: ConditionRepository): Model = ModelImpl(repository)
}