package com.charlag.promind

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 08/05/2017.
 */

@Module
class ActionHandlerModule {
    @Provides fun provideActionHandler(context: Context): ActionHandler = ActionHandlerImpl(context)
}