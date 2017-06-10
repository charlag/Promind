package com.charlag.promind.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.charlag.promind.core.data.models.Action

/**
 * Created by charlag on 25/02/2017.
 *
 * Helper functions which are needed to make Android-specific Intent from platform-independent
 * Action. Keeps our model clean from Android stuff.
 */

fun Action.makeIntent(context: Context): Intent =
        when (this) {
            is Action.OpenMainAction -> getLaunchIntent(context, packageName)
            is Action.UriAction -> getUriIntent(uri)
        }

private fun getLaunchIntent(context: Context, packageName: String): Intent {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return intent
}

private fun getUriIntent(uriString: String): Intent {
    val uri = Uri.parse(uriString)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    return intent
}
