package ru.kn_n.myweather.domain.entities

class ForecastEntity (
    val currentForecast: CurrentWeatherForecastEntity,
    val hourlyForecast: List<HourlyWeatherForecastEntity>
)

class CurrentWeatherForecastEntity(
    val weatherCode: String = "",
    val weatherDescription: String = "",
    val temperature: String = "",
    val feelTemperature: String = "",
    val windSpeed: String = "",
    val windDirection: String = "",
    val pressure: String = "",
    val humidity: String = ""
)

class HourlyWeatherForecastEntity(
    val time: String = "",
    val temperature: String = "",
    val weatherCode: String = ""
)