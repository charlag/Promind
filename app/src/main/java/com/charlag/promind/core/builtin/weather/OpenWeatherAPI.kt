package com.charlag.promind.core.builtin.weather

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by charlag on 11/06/2017.
 */

interface OpenWeatherAPI {
    @GET("data/2.5/weather") fun weatherByCoordinates(@Query("lat") lat: Double,
                                                      @Query("lon") lon: Double): Single<Weather>
    @GET("http://openweathermap.org/img/w/{id}.png") fun getIcon(@Path("id") id: String):
            Single<ResponseBody>
}