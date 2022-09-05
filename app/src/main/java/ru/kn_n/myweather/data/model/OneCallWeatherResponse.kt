package ru.kn_n.myweather.data.model

class OneCallWeatherResponse (
    val timezone: String?,
    val current: CurrentWeatherResponse,
    val hourly: List<HourlyWeatherResponse>?
)

class CurrentWeatherResponse(
    val temp: Double?,
    val feels_like: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val uvi: Double?,
    val wind_speed: Double?,
    val wind_deg: Int?,
    val weather: List<WeatherResponse>?
)

class WeatherResponse(
    val description: String?,
    val icon: String?
)

class HourlyWeatherResponse(
    val dt: Long?,
    val temp: Double?,
    val weather: List<WeatherResponse>?
)
