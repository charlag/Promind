package com.charlag.promind.ui.component.new_hint

import android.content.Context
import com.charlag.promind.core.data.source.ConditionDAO
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 10/04/2017.
 */

@Module
class NewHintPresenterModule(private val view: NewHintContract.View) {
    @Provides
    fun provideNewHintPresenter(context: Context, repository: ConditionDAO): NewHintContract.Presenter
            = NewHintPresenterImpl(view, context, repository)
}