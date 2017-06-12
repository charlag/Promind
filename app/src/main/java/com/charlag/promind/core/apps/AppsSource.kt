package com.charlag.promind.core.apps

import android.graphics.Bitmap

/**
 * Created by charlag on 12/06/2017.
 */

interface AppsSource {
    fun listAllApps(): List<AppData>

    data class AppData(val title: String, val packageName: String, val icon: Bitmap?)
}