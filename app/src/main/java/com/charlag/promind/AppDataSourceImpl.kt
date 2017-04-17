package com.charlag.promind

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by charlag on 17/04/2017.
 */

class AppDataSourceImpl(private val context: Context) : AppDataSource {
    override fun getAppData(packageName: String): AppData {
        val packageManager = context.packageManager
        val name = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(packageName,
                        PackageManager.GET_META_DATA))
                .toString()
        val icon = context.packageManager.getApplicationIcon(packageName)
        return AppData(name, packageName, icon)
    }
}