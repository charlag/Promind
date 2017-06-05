package com.charlag.promind.core.data.source.db

import android.provider.BaseColumns

/**
 * Created by charlag on 25/02/2017.
 */

class ConditionContract {
    object ConditionEntry : BaseColumns {
        val id = BaseColumns._ID
        val count = BaseColumns._COUNT
        val tableName = "condition"
        val latitude = "latitude"
        val longitude = "longitude"
        val locationInverted = "locationInverted"
        val radius = "radius"
        val timeFrom = "timeFrom"
        val timeTo = "timeTo"
        val date = "date"
        val priority = "priority"
        val hint = "hint"
    }
}
