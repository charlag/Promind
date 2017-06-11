package com.charlag.promind.core

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import com.charlag.promind.core.data.models.Action
import java.util.*
import kotlin.Comparator

/**
 * Created by charlag on 15/04/2017.
 *
 * Implementation of the statistics source. It provides hints based on user interaction with the
 * device and sorts them based on frequency of use during the period.
 */

class UsageStatsSourceImpl(private val context: Context) : UsageStatsSource {

    private val filterCalendar = Calendar.getInstance()

    override fun getPackagesUsed(): List<UserHint> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as
                UsageStatsManager
        val packageManager = context.packageManager
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
        val comparator = Comparator<String> { o1, o2 ->
            (frequencies[o2] ?: 0) - (frequencies[o1] ?: 0)
        }

        return frequencies.toSortedMap(comparator).keys
                .map { packageName ->
                    val name = packageManager.getApplicationLabel(
                            packageManager.getApplicationInfo(packageName,
                                    PackageManager.GET_META_DATA))
                            .toString()
                    val icon = context.packageManager.getApplicationIcon(packageName)
                    UserHint(-1, name, icon.toBitmap(), Action.OpenMainAction(packageName))
                }
    }

    private fun Drawable.toBitmap(): Bitmap? {
        if (this is BitmapDrawable) return this.bitmap
        val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        return bitmap
    }

    private fun filterByTime(event: UsageEvents.Event, startMinutes: Int,
                             endMinutes: Int): Boolean {
        val eventMins = filterCalendar.run {
            timeInMillis = event.timeStamp
            minutes
        }
        return eventMins in startMinutes..endMinutes
    }


    private fun filterByType(event: UsageEvents.Event): Boolean {
        val acceptedEvents = mutableListOf(UsageEvents.Event.MOVE_TO_FOREGROUND)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            acceptedEvents += UsageEvents.Event.SHORTCUT_INVOCATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            acceptedEvents += UsageEvents.Event.USER_INTERACTION
        return event.eventType in acceptedEvents
    }

    private fun filterByName(event: UsageEvents.Event): Boolean =
            !event.packageName.startsWith("com.android") && event.packageName != "android"
}

class UsageEventsIterator(val events: UsageEvents) : Iterator<UsageEvents.Event> {
    val event = UsageEvents.Event()
    override fun hasNext(): Boolean = events.hasNextEvent()

    override fun next(): UsageEvents.Event {
        events.getNextEvent(event)
        return event
    }
}

fun UsageEvents.iterator(): Iterator<UsageEvents.Event> = UsageEventsIterator(this)

private val Calendar.minutes: Int
    get() = this.get(Calendar.HOUR_OF_DAY) * 60 + this.get(Calendar.MINUTE)