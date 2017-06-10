package com.charlag.promind.ui.component.widget

import com.charlag.promind.annotation.WidgetScoped
import com.charlag.promind.app.AppComponent
import dagger.Component

/**
 * Created by charlag on 10/06/2017.
 */

@WidgetScoped
@Component(dependencies = arrayOf(AppComponent::class))
interface HintsWidgetComponent {
    fun inject(hintsWidgetProvider: HintsWidgetProvider)
}