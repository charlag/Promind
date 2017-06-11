package com.charlag.promind.core.builtin.weather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.data.models.Action
import io.reactivex.Maybe

/**
 * Created by charlag on 11/06/2017.
 */

class WeatherHintsProviderImpl(val openWeatherAPI: OpenWeatherAPI,
                               val baseUrl: String)
    : WeatherHintsProvider {

    companion object {
        val TAG: String = WeatherHintsProviderImpl::class.java.simpleName
    }

    override fun weatherHints(context: AssistantContext): Maybe<List<UserHint>> {
        if (context.location == null) return Maybe.empty()

        return openWeatherAPI.weatherByCoordinates(context.location.latitude,
                context.location.longitude)
                .doOnError { Log.e(TAG, "Coudln't fetch weather", it) }
                .flatMapMaybe { weather ->
                    val icon: Maybe<Bitmap> = weather.weather.firstOrNull()?.icon?.let {
                        openWeatherAPI.getIcon(it).map {
                            BitmapFactory.decodeStream(it.byteStream())
                        }
                    }?.toMaybe() ?: Maybe.empty()
                    icon.map {
                        val action = Action.UriAction("${baseUrl}city/${weather.id}")
                        listOf(UserHint(-1, weather.main.temp.toString(), it, action))
                    }
                }
    }

}