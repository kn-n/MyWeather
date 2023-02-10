package ru.kn_n.myweather.domain.interactors

import ru.kn_n.myweather.data.repositories.WeatherInfoRepository
import javax.inject.Inject

class WeatherInfoInteractor @Inject constructor(
    private val weatherInfoRepository: WeatherInfoRepository
) {
    suspend fun getWeather(lat: String, lon: String) = weatherInfoRepository.getWeather(lat, lon)
}