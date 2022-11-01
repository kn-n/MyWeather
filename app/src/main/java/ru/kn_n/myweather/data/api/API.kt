package ru.kn_n.myweather.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kn_n.myweather.data.model.GeocodingResponse
import ru.kn_n.myweather.data.model.WeatherResponse

interface API {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("hourly") temp: String = "temperature_2m",
        @Query("hourly") humidity: String = "relativehumidity_2m",
        @Query("hourly") feelTemp: String = "apparent_temperature",
        @Query("hourly") weatherCode: String = "weathercode",
        @Query("hourly") pressure: String = "surface_pressure",
        @Query("current_weather") current_weather: String = "true",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse

    @GET("search")
    suspend fun getPlaces(
        @Query("name") name: String,
        @Query("language") language: String = "ru"
    ): GeocodingResponse
}