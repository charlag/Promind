package com.charlag.promind.core.apps

import android.content.Context
import android.content.pm.PackageManager
import com.charlag.promind.util.toBitmap

/**
 * Created by charlag on 12/06/2017.
 */

internal class AppsSourceImpl(context: Context) : AppsSource {
    private val packageManager = context.packageManager

    override fun listAllApps(): List<AppsSource.AppData> =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                    .fold(listOf()) { l, app ->
                        val title = packageManager.getApplicationLabel(app)?.toString() ?:
                                return@fold l

                        val icon = packageManager.getApplicationIcon(app)
                        l + AppsSource.AppData(title, app.packageName, icon.toBitmap())
                    }
}