package com.charlag.promind.core.model

import com.charlag.promind.core.data.models.AssistantContext
import com.charlag.promind.core.data.models.UserHint
import io.reactivex.Observable

/**
 * Created by charlag on 11/02/2017.
 *
 * The core of the app, responsible for making a list of conditions based on the context.
 */

interface Model {
    fun getHintsForContext(context: AssistantContext): Observable<List<UserHint>>
}
