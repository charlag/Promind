package com.charlag.promind.core

import android.location.Location

/**
 * Created by charlag on 25/02/2017.
 *
 * Data class used by database and model to match location and time to the actions
 */

data class Condition(val location: Location?, val timeInterval: TimeInterval?, val hint: UserHint)