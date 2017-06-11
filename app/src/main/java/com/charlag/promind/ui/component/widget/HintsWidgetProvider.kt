package com.charlag.promind.ui.component.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.charlag.promind.R

/**
 * Created by charlag on 15/02/2017.
 */

class HintsWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {

        appWidgetIds.forEach { id ->
            val intent = Intent(context, WidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.hints_widget_layout)
            views.setRemoteAdapter(R.id.containter_hints_widget, intent)
            views.setEmptyView(R.id.containter_hints_widget, R.id.empty_view)
            views.setPendingIntentTemplate(R.id.containter_hints_widget,
                    PendingIntent.getService(context, 0,
                            Intent(context, ActionService::class.java), 0))
            appWidgetManager.updateAppWidget(id, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}