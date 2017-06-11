package com.charlag.promind.ui.component.widget

import android.app.IntentService
import android.content.Intent

/**
 * Created by charlag on 11/06/2017.
 */

class ActionService @SuppressWarnings("unused") constructor()
    : IntentService("ActionSerivce") {
    override fun onHandleIntent(intent: Intent) {
        val originalIntent = Intent.parseUri(intent.dataString, Intent.URI_INTENT_SCHEME)
        startActivity(originalIntent)
    }

}