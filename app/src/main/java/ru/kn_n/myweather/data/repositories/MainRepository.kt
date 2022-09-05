package ru.kn_n.myweather.data.repositories

import ru.kn_n.myweather.data.api.APIHelper
import ru.kn_n.myweather.data.mappers.OneCallWeatherResponseMapper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: APIHelper) {
    suspend fun getWeather(lat: String, lon: String) = OneCallWeatherResponseMapper().mapOneCallWeatherResponse(apiHelper.getWeather(lat, lon))
}