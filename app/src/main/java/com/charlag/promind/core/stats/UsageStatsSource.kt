package com.charlag.promind.core.stats

import com.charlag.promind.core.UserHint

/**
 * Created by charlag on 15/04/2017.
 */

interface UsageStatsSource {
    fun getPackagesUsed(): List<UserHint>
}