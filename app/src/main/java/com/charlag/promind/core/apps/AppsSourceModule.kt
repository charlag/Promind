package com.charlag.promind.core.apps

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 12/06/2017.
 */

@Module
class AppsSourceModule {
    @Provides fun appsSource(context: Context): AppsSource = AppsSourceImpl(context)
}