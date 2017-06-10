package com.charlag.promind.ui.component.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.charlag.promind.R
import com.charlag.promind.app.App
import com.charlag.promind.core.app_data.AppDataProvider
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.util.makeIntent
import javax.inject.Inject

/**
 * Created by charlag on 15/02/2017.
 */

class HintsWidgetProvider : AppWidgetProvider() {

    @Inject lateinit var repository: HintsRepository
    @Inject lateinit var appDataProvider: AppDataProvider

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        DaggerHintsWidgetComponent.builder()
                .appComponent(App.graph)
                .build()
                .inject(this)

        appWidgetIds.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.hints_widget_layout)

            val result = goAsync()
            repository.hints.take(2)
                    .doOnTerminate { result.finish() }
                    .subscribe { hints ->
                        hints.map { hint ->
                            val button = RemoteViews(context.packageName, R.layout.hint_widget)
                            button.setTextViewText(R.id.widget_hint_text, hint.title)
                            val intent = hint.action.makeIntent(context)
                            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                            button.setOnClickPendingIntent(R.id.widget_hint_text, pendingIntent)
                            button
                        }.forEach { views.addView(R.id.containter_hints_widget, it) }
                        appWidgetManager.updateAppWidget(id, views)
                    }

        }
    }
}