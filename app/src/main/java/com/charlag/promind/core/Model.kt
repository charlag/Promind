package com.charlag.promind.core

import io.reactivex.Observable

/**
 * Created by charlag on 11/02/2017.
 */

interface Model {
    fun getHintsForContext(context: AssistantContext): Observable<List<UserHint>>
}
