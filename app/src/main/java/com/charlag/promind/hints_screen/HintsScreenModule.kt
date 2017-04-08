package com.charlag.promind.hints_screen

import com.charlag.promind.LocationProvider
import com.charlag.promind.core.Model
import com.charlag.promind.core.context_data.DateProvider
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 27/03/2017.
 */

@Module
class HintsScreenModule {
    @Provides
    fun providesViewModel(model: Model, dateProvider: DateProvider,
                          locationProvider: LocationProvider): HintsScreenViewModel =
            HintsScreenViewModelImpl(model, locationProvider, dateProvider)
}