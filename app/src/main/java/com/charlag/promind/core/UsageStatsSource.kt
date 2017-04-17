package com.charlag.promind.core

/**
 * Created by charlag on 15/04/2017.
 */

interface UsageStatsSource {
    fun getPackagesUsed(): List<String>
}