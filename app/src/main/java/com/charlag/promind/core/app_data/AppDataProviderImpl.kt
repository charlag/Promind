package com.charlag.promind.core.app_data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

/**
 * Created by charlag on 17/04/2017.
 */

class AppDataProviderImpl(private val context: Context) : AppDataProvider {
    override fun getAppData(packageName: String): AppData {
        val packageManager = context.packageManager
        try {
            val name = packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA))
                    .toString()
            val icon = context.packageManager.getApplicationIcon(packageName)
            return AppData(name, packageName, icon)
        } catch (e: PackageManager.NameNotFoundException) {
            return AppData("*deleted*", packageName, ColorDrawable(Color.TRANSPARENT))
        }
    }
}