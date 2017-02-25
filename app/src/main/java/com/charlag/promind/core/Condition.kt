package com.charlag.promind.core

import java.sql.Date

/**
 * Created by charlag on 25/02/2017.
 *
 * Data class used by database and model to match location and time to the actions
 */

data class Condition(val location: Location?, val timeFrom: Int?, val timeTo: Int?,
                     val date: Date?, val hint: UserHint, val locationInverted: Boolean = false)