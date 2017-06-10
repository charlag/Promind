package com.charlag.promind.core.data.models

import com.charlag.promind.core.UserHint
import java.util.*

/**
 * Created by charlag on 25/02/2017.
 *
 * Data class used by database and model to match location and time to the actions
 */

data class Condition(val timeFrom: Int?, val timeTo: Int?, val date: Date?,
                     val location: Location?,
                     val radius: Int?,
                     val locationInverted: Boolean = false,
                     val hint: UserHint)