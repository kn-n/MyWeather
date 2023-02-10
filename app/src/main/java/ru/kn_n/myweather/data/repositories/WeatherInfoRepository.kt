package ru.kn_n.myweather.data.repositories

import ru.kn_n.myweather.data.api.APIWeather
import ru.kn_n.myweather.data.mappers.WeatherResponseMapper
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor(private val apiWeather: APIWeather) {
    suspend fun getWeather(lat: String, lon: String) =
        WeatherResponseMapper().mapWeatherResponse(apiWeather.getWeather(lat, lon))
}