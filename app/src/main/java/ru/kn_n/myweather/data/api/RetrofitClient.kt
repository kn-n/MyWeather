package ru.kn_n.myweather.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kn_n.myweather.utils.Constants.BASE_URL_GEOCODING
import ru.kn_n.myweather.utils.Constants.BASE_URL_WEATHER

object RetrofitClient {
    private val retrofitForWeather = Retrofit.Builder()
        .baseUrl(BASE_URL_WEATHER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    fun buildServiceWeather(): API {
        return retrofitForWeather
    }

    private val retrofitForGeocoding = Retrofit.Builder()
        .baseUrl(BASE_URL_GEOCODING)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    fun buildServiceGeocoding(): API {
        return retrofitForGeocoding
    }
}
