package com.charlag.promind

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.ModelImpl
import com.charlag.promind.core.data.source.ConditionDbRepository
import com.charlag.promind.core.data.source.db.ConditionDbHelper
import java.util.*

/**
 * Created by charlag on 15/02/2017.
 */

class HintsWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds.forEach { id ->
//            val views = RemoteViews(context.packageName, R.layout.hints_widget_layout)
//
//            val repository = ConditionDbRepository(ConditionDbHelper(context))
//            val model = ModelImpl(repository)
//            val assistantContext = AssistantContext(null, Date())
//            model.getHintsForContext(assistantContext).subscribe { hints ->
//                hints.map { (_, title, action) ->
//                    val button = RemoteViews(context.packageName, R.layout.hint_widget)
//                    button.setTextViewText(R.id.widget_hint_text, title)
//                    val intent = action.makeIntent(context)
//                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//                    button.setOnClickPendingIntent(R.id.widget_hint_text, pendingIntent)
//                    button
//                }
//                        .forEach { views.addView(R.id.containter_hints_widget, it) }
//            }
//            appWidgetManager.updateAppWidget(id, views)
        }
    }
}