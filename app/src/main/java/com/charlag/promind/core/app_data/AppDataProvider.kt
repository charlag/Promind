package com.charlag.promind.core.app_data

/**
 * Created by charlag on 17/04/2017.
 */

interface AppDataProvider {
    fun getAppData(packageName: String): AppData
}