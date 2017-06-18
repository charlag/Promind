package com.charlag.promind.ui.component.new_hint

import com.charlag.promind.app.AppComponent
import com.charlag.promind.annotation.ActivityScoped
import com.charlag.promind.ui.frame.FrameModule
import dagger.Component

/**
 * Created by charlag on 10/04/2017.
 */

@ActivityScoped
@Component(dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(NewHintPresenterModule::class, FrameModule::class))
interface NewHintComponent {
    fun inject(activity: NewHintFragment)
}