package com.charlag.promind.submit

import com.charlag.promind.app.AppComponent
import dagger.Component

/**
 * Created by charlag on 12/06/2017.
 */

@Component(dependencies = arrayOf(AppComponent::class))
interface SubmitComponent {
    fun inject(component: SubmitComponent)
}