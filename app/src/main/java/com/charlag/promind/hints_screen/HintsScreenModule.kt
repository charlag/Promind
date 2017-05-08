package com.charlag.promind.hints_screen

import com.charlag.promind.ActionHandler
import com.charlag.promind.AppDataSource
import com.charlag.promind.LocationProvider
import com.charlag.promind.core.Model
import com.charlag.promind.core.context_data.DateProvider
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 27/03/2017.
 */

@Module
class HintsScreenModule(private val view: HintsScreenContract.View) {
    @Provides
    fun providesViewModel(model: Model, dateProvider: DateProvider,
                          locationProvider: LocationProvider,
                          appDataSource: AppDataSource,
                          actionHandler: ActionHandler): HintsScreenContract.Presenter =
            HintsScreenPresenter(model, view, locationProvider, dateProvider,
                    appDataSource, actionHandler)
}