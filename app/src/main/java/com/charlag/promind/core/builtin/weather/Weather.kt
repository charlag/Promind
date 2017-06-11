package com.charlag.promind.core.builtin.weather

/**
 * Created by charlag on 11/06/2017.
 */

data class Weather(val id: Long, val name: String, val main: Main, val wind: Wind,
                   val weather: List<Conditions>) {
    data class Main(val temp: Double, val pressure: Double, val tempMin: Double,
                    val tempMax: Double)
    data class Wind(val speed: Double)
    data class Conditions(val main: String, val description: String, val icon: String)
}