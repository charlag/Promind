package com.charlag.promind.core.context_data

import android.content.Context
import com.charlag.promind.LocationProvider
import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 08/04/2017.
 */

@Module
class ContextDataModule {
    @Provides
    fun provideLocationProvider(context: Context): LocationProvider = LocationProviderImpl(context)

    @Provides
    fun provideDateProvider(): DateProvider = DateProviderImpl()
}