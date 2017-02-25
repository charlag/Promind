package com.charlag.promind.core

/**
 * Created by charlag on 11/02/2017.
 */

interface Model {
    fun getHintsForContext(context: AssistantContext): List<UserHint>
}
