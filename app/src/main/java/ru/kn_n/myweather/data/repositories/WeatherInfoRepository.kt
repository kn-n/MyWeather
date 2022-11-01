package ru.kn_n.myweather.data.repositories

import ru.kn_n.myweather.data.api.APIHelper
import ru.kn_n.myweather.data.mappers.WeatherResponseMapper
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor(private val apiHelper: APIHelper) {
    suspend fun getWeather(lat: String, lon: String) = WeatherResponseMapper().mapWeatherResponse(apiHelper.getWeather(lat, lon))
}