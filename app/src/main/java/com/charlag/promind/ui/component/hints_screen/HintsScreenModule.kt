package com.charlag.promind.ui.component.hints_screen

import com.charlag.promind.core.action.ActionHandler
import com.charlag.promind.core.app_data.AppDataProvider
import com.charlag.promind.core.context_data.LocationProvider
import com.charlag.promind.core.Model
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.repository.HintsRepository
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 27/03/2017.
 */

@Module
class HintsScreenModule {
    @Provides
    fun providesViewModel(hintsRepository: HintsRepository,
                          appDataSource: AppDataProvider,
                          actionHandler: ActionHandler): HintsScreenContract.Presenter =
            HintsScreenPresenter(hintsRepository, appDataSource, actionHandler)
}