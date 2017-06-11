package com.charlag.promind.core.builtin.weather

import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.UserHint
import io.reactivex.Maybe

/**
 * Created by charlag on 11/06/2017.
 */

interface WeatherHintsProvider {
    fun weatherHints(context: AssistantContext): Maybe<List<UserHint>>
}