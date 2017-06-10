package com.charlag.promind.ui.component.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
                            val hintView = RemoteViews(context.packageName,
                                    R.layout.hint_widget)
                            hintView.setTextViewText(R.id.widget_hint_text, hint.title)
                            val intent = hint.action.makeIntent(context)
                            val pendingIntent = PendingIntent.getActivity(context, 0,
                                    intent, 0)
                            hintView.setOnClickPendingIntent(R.id.widget_hint_text,
                                    pendingIntent)
                            hintView
                        }.forEach { views.addView(R.id.containter_hints_widget, it) }
                        appWidgetManager.updateAppWidget(id, views)
                    }

        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) return drawable.bitmap
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}