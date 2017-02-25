package com.charlag.promind.core

import java.util.*

/**
 * Created by charlag on 25/02/2017.
 */

class TimeInterval(private val fromTime: Date, private val toTime: Date, private val exact: Boolean) {
    fun inInterval(date: Date, calendar: Calendar): Boolean =
        if (exact) fromTime.before(date) && toTime.after(date) else {
            calendar.time = date
            val timeNow = calendar.getTimeInDay()
            calendar.time = fromTime
            val timeFrom = calendar.getTimeInDay()
            calendar.time = toTime
            val timeTo = calendar.getTimeInDay()
            timeFrom <= timeNow && timeNow <+ timeTo
        }
}

fun Calendar.getTimeInDay(): Int = get(Calendar.HOUR_OF_DAY) * 60 + get(Calendar.MINUTE)