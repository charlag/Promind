package com.charlag.promind.core.data.models

import com.charlag.promind.core.data.source.db.HintContract.*

/**
 * Created by charlag on 11/02/2017.
 *
 * Abstraction of possible action as a reaction on user selection.
 * Not using Intent for this to be independent from Android.
 */

sealed class Action {
    class OpenMainAction(val packageName: String) : Action()
    class UriAction(val uri: String) : Action()

    val type: String
        get() = when (this) {
            is OpenMainAction -> HintActionType.openMain
            is UriAction -> HintActionType.url
        }

    val data: String
        get() = when (this) {
            is OpenMainAction -> packageName
            is UriAction -> uri
        }

    override fun equals(other: Any?): Boolean =
            (this is OpenMainAction && other is OpenMainAction &&
                    this.packageName == other.packageName) ||
                    (this is UriAction && other is UriAction &&
                            this.uri == other.uri)

    override fun hashCode(): Int =
            when (this) {
                is OpenMainAction -> ("OpenMainAction" + packageName).hashCode()
                is UriAction -> ("UriAction" + uri).hashCode()
            }
}
