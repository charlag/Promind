package com.charlag.promind.core

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.*

/**
 * Created by charlag on 15/04/2017.
 */

class UsageStatsSourceImpl(private val context: Context) : UsageStatsSource {
    override fun getPackagesUsed(): List<String> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as
                UsageStatsManager
        val start = Calendar.getInstance().run {
            add(Calendar.DAY_OF_MONTH, -2)
            add(Calendar.HOUR_OF_DAY, -1)
            timeInMillis
        }
        val end = Calendar.getInstance().run {
            add(Calendar.DAY_OF_MONTH, -1)
            add(Calendar.HOUR_OF_DAY, 1)
            timeInMillis
        }
        val startTime = Calendar.getInstance().minutes
        val endTime = Calendar.getInstance().run {
            add(Calendar.HOUR_OF_DAY, 1)
            minutes
        }

        val events = usageStatsManager.queryEvents(start, end)
        val packages = mutableSetOf<String>()
        val event = UsageEvents.Event()
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.USER_INTERACTION
                    || event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND
                    || event.eventType == UsageEvents.Event.SHORTCUT_INVOCATION) {
                val eventHour = Calendar.getInstance().run {
                    timeInMillis = event.timeStamp
                    minutes
                }
                if (eventHour in startTime..endTime) {
                    packages.add(event.packageName)
                }
            }
        }
        return packages.toList()
    }
}

private val Calendar.minutes: Int
    get() = this.get(Calendar.HOUR_OF_DAY) * 60 + this.get(Calendar.MINUTE)