package com.charlag.promind.core.action

import android.content.Context
import com.charlag.promind.core.data.models.Action
import com.charlag.promind.util.makeIntent

/**
 * Created by charlag on 17/04/2017.
 */

class ActionHandlerImpl(private val context: Context) : ActionHandler {
    override fun handle(action: Action) = context.startActivity(action.makeIntent(context))
}