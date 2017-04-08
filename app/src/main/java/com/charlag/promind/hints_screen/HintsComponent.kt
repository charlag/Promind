package com.charlag.promind.hints_screen

import com.charlag.promind.AppComponent
import com.charlag.promind.MainActivity
import com.charlag.promind.annotation.ActivityScoped
import dagger.Component

/**
 * Created by charlag on 27/03/2017.
 */

@ActivityScoped
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(HintsScreenModule::class))
interface HintsComponent {
    fun inject(screen: MainActivity)
}
