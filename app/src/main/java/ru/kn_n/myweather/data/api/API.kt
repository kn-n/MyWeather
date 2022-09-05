package ru.kn_n.myweather.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kn_n.myweather.data.model.OneCallWeatherResponse
import ru.kn_n.myweather.utils.Constants.API_KEY

interface API {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
        @Query("appid") appid: String = API_KEY
    ): OneCallWeatherResponse
}