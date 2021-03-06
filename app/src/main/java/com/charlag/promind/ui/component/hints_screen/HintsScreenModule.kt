package com.charlag.promind.ui.component.hints_screen

import com.charlag.promind.core.action.ActionHandler
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.ui.frame.Navigator
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 27/03/2017.
 */

@Module
class HintsScreenModule {
    @Provides fun providesViewModel(hintsRepository: HintsRepository,
                                    actionHandler: ActionHandler, navigator: Navigator):
            HintsScreenContract.Presenter
            = HintsScreenPresenter(hintsRepository, actionHandler, navigator)
}