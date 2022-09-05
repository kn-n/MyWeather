package ru.kn_n.myweather.data.api

import toothpick.InjectConstructor

@InjectConstructor
class APIHelper {
    suspend fun getWeather(lat: String, lon: String) =
        RetrofitClient.buildService().getWeather(lat, lon)
}