package com.charlag.promind.util

import io.reactivex.functions.Function

/**
 * Created by charlag on 10/04/2017.
 */

object Empty {
    object map : Function<Any, Empty> {
        override fun apply(t: Any?) = Empty
    }
}