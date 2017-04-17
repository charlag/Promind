package com.charlag.promind.new_hint

import com.charlag.promind.AppComponent
import com.charlag.promind.annotation.ActivityScoped
import dagger.Component

/**
 * Created by charlag on 10/04/2017.
 */

@ActivityScoped
@Component(dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(NewHintPresenterModule::class))
interface NewHintComponent {
    fun inject(activity: NewHintActivity)
}