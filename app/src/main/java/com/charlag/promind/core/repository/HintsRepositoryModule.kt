package com.charlag.promind.core.repository

import com.charlag.promind.core.model.Model
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.context_data.LocationProvider
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 10/06/2017.
 */

@Module
class HintsRepositoryModule {
    @Provides fun providesHintsRepository(model: Model, locationProvider: LocationProvider,
                                          dateProvider: DateProvider): HintsRepository =
            HintsRepositoryImpl(model, locationProvider, dateProvider)
}