package com.charlag.promind.ui.component.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.charlag.promind.R
import com.charlag.promind.app.App
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.util.makeIntent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by charlag on 11/06/2017.
 */

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return HintRemoteViewsFactory(applicationContext, intent)
    }
}

class HintRemoteViewsFactory(val context: Context,
                             intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    @Inject lateinit var repository: HintsRepository
    val hints = mutableListOf<UserHint>()

//    val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
//            AppWidgetManager.INVALID_APPWIDGET_ID)

    override fun onCreate() {
        DaggerHintsWidgetComponent.builder()
                .appComponent(App.graph)
                .build().inject(this)
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
        repository.hints
                .take(3)
                .timeout(8, TimeUnit.SECONDS)
                .onErrorReturn { listOf() }
                .blockingSubscribe { hints.clear(); hints += it }
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val hint = hints[position]
        val hintView = RemoteViews(context.packageName,
                R.layout.hint_widget)
        hintView.setTextViewText(R.id.widget_hint_text, hint.title)
        val intent = hint.action.makeIntent(context)
        val fillInIntent = Intent()
        fillInIntent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        hintView.setOnClickFillInIntent(R.id.widget_hint_container, fillInIntent)
        hintView.setImageViewBitmap(R.id.widget_hint_image, hint.icon)
        return hintView
    }

    override fun getViewTypeCount(): Int = 1

    override fun getCount(): Int = hints.size

    override fun onDestroy() {
    }

}