package ru.kn_n.myweather.entities

import ru.kn_n.myweather.data.model.CurrentWeatherResponse
import ru.kn_n.myweather.data.model.HourlyWeatherResponse
import ru.kn_n.myweather.data.model.WeatherResponse
import java.util.*

class ForecastEntity (
    val currentForecast: CurrentWeatherForecastEntity,
    val hourlyForecast: List<HourlyWeatherForecastEntity>
)

class CurrentWeatherForecastEntity(
    val temp: String = "",
    val feelsLike: String = "",
    val pressure: String = "",
    val humidity: String = "",
    val uvi: String = "",
    val windSpeed: String = "",
    val windDeg: String = "",
    val weather: List<WeatherEntity>
)

class WeatherEntity(
    val description: String = "",
    val iconId: String = ""
)

class HourlyWeatherForecastEntity(
    val time: String = "",
    val temp: String = "",
    val weather: List<WeatherEntity>
)