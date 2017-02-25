package com.charlag.promind.core

/**
 * Created by charlag on 11/02/2017.
 */

sealed class Action {
    class OpenMainAction(val packageName: String): Action()
    class UriAction(val uri: String): Action()
}
