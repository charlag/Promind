package com.charlag.promind.ui.frame

import dagger.Module
import dagger.Provides

/**
 * Created by charlag on 18/06/2017.
 */

@Module
class FrameModule(val actitity: MainActivity) {
    @Provides fun navigator(): Navigator = NavigatorImpl(actitity)
}