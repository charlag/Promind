package com.charlag.promind

import android.content.Context
import com.charlag.promind.core.data.Action

/**
 * Created by charlag on 17/04/2017.
 */

class ActionHandlerImpl(private val context: Context) : ActionHandler {
    override fun handle(action: Action) = context.startActivity(action.makeIntent(context))
}