package com.charlag.promind.core.data

/**
 * Created by charlag on 11/02/2017.
 *
 * Abstraction of possible action as a reaction on user selection.
 * Not using Intent for this to be independent from Android.
 */

sealed class Action {
    class OpenMainAction(val packageName: String) : Action()
    class UriAction(val uri: String) : Action()


    override fun equals(other: Any?): Boolean =
            (this is OpenMainAction && other is OpenMainAction &&
                    this.packageName == other.packageName) ||
                    (this is UriAction && other is UriAction &&
                            this.uri == other.uri)

    override fun hashCode(): Int =
            if (this is OpenMainAction) ("OpenMainAction" + packageName).hashCode()
            else if (this is UriAction) ("UriAction" + uri).hashCode()
            else 0

}
