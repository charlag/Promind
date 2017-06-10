package com.charlag.promind.core.action

import android.content.Context

/**
 * Created by charlag on 08/05/2017.
 */

@dagger.Module
class ActionHandlerModule {
    @dagger.Provides fun provideActionHandler(context: Context): ActionHandler =
            ActionHandlerImpl(context)
}