package com.charlag.promind

import com.charlag.promind.core.data.Action

/**
 * Created by charlag on 17/04/2017.
 */

interface ActionHandler {
    fun handle(action: Action)
}
