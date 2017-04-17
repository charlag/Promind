package com.charlag.promind

import com.charlag.promind.AppData

/**
 * Created by charlag on 17/04/2017.
 */

interface AppDataSource {
    fun getAppData(packageName: String): AppData
}