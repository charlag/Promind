package com.charlag.promind.core.data.models

import java.util.*

/**
 * Created by charlag on 25/02/2017.
 *
 * Represents a condition which should hold in order for hint to be shown.
 */

data class Condition(val timeFrom: Int?, val timeTo: Int?, val date: Date?,
                     val location: Location?,
                     val radius: Int?,
                     val locationInverted: Boolean = false,
                     val hint: UserHint)