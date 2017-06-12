package com.charlag.promind.ui.component.new_hint

import android.content.Context
import com.charlag.promind.core.apps.AppsSource
import com.charlag.promind.core.data.source.ConditionDAO
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 10/04/2017.
 */

@Module
class NewHintPresenterModule {
    @Provides
    fun provideNewHintPresenter(dao: ConditionDAO, appsSource: AppsSource): NewHintContract.Presenter
            = NewHintPresenterImpl(dao, appsSource)
}