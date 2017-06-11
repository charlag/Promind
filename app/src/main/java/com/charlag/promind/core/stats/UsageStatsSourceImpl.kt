package com.charlag.promind.core.stats

import com.charlag.promind.core.data.models.Action

/**
 * Created by charlag on 15/04/2017.
 *
 * Implementation of the statistics source. It provides hints based on user interaction with the
 * device and sorts them based on frequency of use during the period.
 */

class UsageStatsSourceImpl(private val context: android.content.Context) : UsageStatsSource {

    private val filterCalendar = java.util.Calendar.getInstance()

    override fun getPackagesUsed(): List<com.charlag.promind.core.UserHint> {
        val usageStatsManager = context.getSystemService(android.content.Context.USAGE_STATS_SERVICE) as
                android.app.usage.UsageStatsManager
        val packageManager = context.packageManager
        val start = java.util.Calendar.getInstance().run {
            add(java.util.Calendar.DAY_OF_MONTH, -2)
            add(java.util.Calendar.HOUR_OF_DAY, -1)
            timeInMillis
        }
        val end = java.util.Calendar.getInstance().run {
            add(java.util.Calendar.DAY_OF_MONTH, -1)
            add(java.util.Calendar.HOUR_OF_DAY, 1)
            timeInMillis
        }
        val startTime = java.util.Calendar.getInstance().minutes
        val endTime = java.util.Calendar.getInstance().run {
            add(java.util.Calendar.HOUR_OF_DAY, 1)
            minutes
        }

        val events = usageStatsManager.queryEvents(start, end)

        val frequencies = events.iterator().asSequence().filter { event ->
            filterByType(event)
                    && filterByName(event)
                    && filterByTime(event, startTime, endTime)
        }.fold(mutableMapOf<String, Int>()) { map, event ->
            val frequency = map[event.packageName] ?: 0
            map[event.packageName] = frequency + 1
            map
        }

        // sort packages by frequency
        val comparator = kotlin.Comparator<String> { o1, o2 ->
            (frequencies[o2] ?: 0) - (frequencies[o1] ?: 0)
        }

        return frequencies.toSortedMap(comparator).keys
                .map { packageName ->
                    val name = packageManager.getApplicationLabel(
                            packageManager.getApplicationInfo(packageName,
                                    android.content.pm.PackageManager.GET_META_DATA))
                            .toString()
                    val icon = context.packageManager.getApplicationIcon(packageName)
                    com.charlag.promind.core.UserHint(-1, name, icon.toBitmap(),
                            Action.OpenMainAction(packageName))
                }
    }

    private fun android.graphics.drawable.Drawable.toBitmap(): android.graphics.Bitmap? {
        if (this is android.graphics.drawable.BitmapDrawable) return this.bitmap
        val bitmap = android.graphics.Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight,
                android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        return bitmap
    }

    private fun filterByTime(event: android.app.usage.UsageEvents.Event, startMinutes: Int,
                             endMinutes: Int): Boolean {
        val eventMins = filterCalendar.run {
            timeInMillis = event.timeStamp
            minutes
        }
        return eventMins in startMinutes..endMinutes
    }


    private fun filterByType(event: android.app.usage.UsageEvents.Event): Boolean {
        val acceptedEvents = mutableListOf(android.app.usage.UsageEvents.Event.MOVE_TO_FOREGROUND)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1)
            acceptedEvents += android.app.usage.UsageEvents.Event.SHORTCUT_INVOCATION
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            acceptedEvents += android.app.usage.UsageEvents.Event.USER_INTERACTION
        return event.eventType in acceptedEvents
    }

    private fun filterByName(event: android.app.usage.UsageEvents.Event): Boolean =
            !event.packageName.startsWith("com.android") && event.packageName != "android"
}

class UsageEventsIterator(val events: android.app.usage.UsageEvents) : Iterator<android.app.usage.UsageEvents.Event> {
    val event = android.app.usage.UsageEvents.Event()
    override fun hasNext(): Boolean = events.hasNextEvent()

    override fun next(): android.app.usage.UsageEvents.Event {
        events.getNextEvent(event)
        return event
    }
}

fun android.app.usage.UsageEvents.iterator(): Iterator<android.app.usage.UsageEvents.Event> = com.charlag.promind.core.stats.UsageEventsIterator(
        this)

private val java.util.Calendar.minutes: Int
    get() = this.get(java.util.Calendar.HOUR_OF_DAY) * 60 + this.get(java.util.Calendar.MINUTE)