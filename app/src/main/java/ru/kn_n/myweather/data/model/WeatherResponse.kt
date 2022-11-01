package ru.kn_n.myweather.data.model

class WeatherResponse(
    val current_weather: CurrentWeatherResponse,
    val hourly: HourlyWeatherResponse
)

class CurrentWeatherResponse(
    val temperature: Double?,
    val windspeed: Double?,
    val winddirection: Double?,
    val weathercode: Int?,
    val time: String?
)

class HourlyWeatherResponse(
    val time: List<String>?,
    val temperature_2m: List<Double>?,
    val relativehumidity_2m: List<Int>?,
    val apparent_temperature: List<Double>?,
    val weathercode: List<Int>?,
    val surface_pressure: List<Double>?
)
