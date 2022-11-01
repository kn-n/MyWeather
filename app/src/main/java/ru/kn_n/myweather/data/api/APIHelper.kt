package ru.kn_n.myweather.data.api

import toothpick.InjectConstructor

@InjectConstructor
class APIHelper {
    suspend fun getWeather(lat: String, lon: String) =
        RetrofitClient.buildServiceWeather().getWeather(lat, lon)

    suspend fun getPlaces(name: String) =
        RetrofitClient.buildServiceGeocoding().getPlaces(name)
}