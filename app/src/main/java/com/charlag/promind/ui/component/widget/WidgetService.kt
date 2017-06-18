package com.charlag.promind.ui.component.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.charlag.promind.R
import com.charlag.promind.core.data.models.UserHint
import com.charlag.promind.core.repository.HintsRepository
import com.charlag.promind.util.appComponent
import com.charlag.promind.util.makeIntent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by charlag on 11/06/2017.
 */

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return HintRemoteViewsFactory(applicationContext)
    }
}

class HintRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {
    @Inject lateinit var repository: HintsRepository
    val hints = mutableListOf<UserHint>()

    override fun onCreate() {
        DaggerHintsWidgetComponent.builder()
                .appComponent(context.appComponent)
                .build().inject(this)
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
        // kind of a hack to subscribe to location updates
        if (Looper.myLooper() == null) Looper.prepare()
        // kind of a hack to get data but to not wait forever
        repository.hints
                .take(3)
                .timeout(8, TimeUnit.SECONDS)
                .doOnNext { hints.clear(); hints.addAll(it) }
                .onErrorReturn { listOf() }
                .blockingSubscribe()
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
        Looper.myLooper().quitSafely()
    }

}