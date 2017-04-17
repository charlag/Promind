package com.charlag.promind.core

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import java.util.*

/**
 * Created by charlag on 15/04/2017.
 */

class UsageStatsSourceImpl(private val context: Context) : UsageStatsSource {
    override fun getPackagesUsed(): List<String> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as
                UsageStatsManager
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val end = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, -2)
        val start = cal.timeInMillis
        val timeStart = Calendar.getInstance().apply {
            add(Calendar.HOUR, -1)
        }
        val timeEnd = Calendar.getInstance()
        return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, start, end)
//                .filter { it.totalTimeInForeground > TimeUnit.MINUTES.toMillis(1) }
                .filter {
                    cal.timeInMillis = it.lastTimeUsed
                    cal.set(Calendar.YEAR, timeEnd.get(Calendar.YEAR))
                    cal.set(Calendar.MONTH, timeEnd.get(Calendar.MONTH))
                    cal.set(Calendar.DAY_OF_MONTH, timeEnd.get(Calendar.DAY_OF_MONTH))
                    cal.before(timeEnd) && cal.after(timeStart)
                }
                .map { it.packageName }
    }
}