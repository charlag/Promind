package com.charlag.promind.core.builtin.weather

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

/**
 * Created by charlag on 11/06/2017.
 */

@Module
class WeatherHintsModule {
    @Provides fun weatherApi(retrofit: Retrofit): OpenWeatherAPI =
            retrofit.create(OpenWeatherAPI::class.java)

    @Provides fun retrofit(okHttpClient: OkHttpClient,
                           adapterFactory: CallAdapter.Factory,
                           converterFactory: Converter.Factory,
                           @Named("baseUrl") baseUrl: String): Retrofit =
            Retrofit.Builder()
                    .client(okHttpClient)
                    .addCallAdapterFactory(adapterFactory)
                    .addConverterFactory(converterFactory)
                    .baseUrl(baseUrl)
                    .build()

    @Provides @Named("baseUrl") fun baseUrl(): String = "http://api.openweathermap.org/"

    @Provides @Named("weatherAppId") fun weatherAppId(): String = "f4ef835d36779bb5be4de0dd4ce981d6"

    @Provides fun okHttpClient(@Named("weatherAppId") weatherAppId: String): OkHttpClient =
            OkHttpClient.Builder()
                    .addNetworkInterceptor { chain ->
                        val newUrl = chain.request().url().newBuilder().addQueryParameter(
                                "APPID", weatherAppId)
                                .build()
                        val newRequest = chain.request().newBuilder()
                                .url(newUrl).build()
                        chain.proceed(newRequest)
                    }
                    .build()

    @Provides fun adapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.createAsync()

    @Provides fun converterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Provides fun weatherHintsProvider(weatherAPI: OpenWeatherAPI): WeatherHintsProvider =
            WeatherHintsProviderImpl(weatherAPI, "https://openweathermap.org/")

}