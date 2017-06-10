package com.charlag.promind.core.action

import com.charlag.promind.core.data.models.Action

/**
 * Created by charlag on 17/04/2017.
 */

interface ActionHandler {
    fun handle(action: Action)
}
