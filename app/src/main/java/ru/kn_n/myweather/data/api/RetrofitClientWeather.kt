package ru.kn_n.myweather.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kn_n.myweather.utils.Constants.BASE_URL_WEATHER
import javax.inject.Inject
import javax.inject.Provider

class RetrofitClientWeather @Inject constructor() : Provider<APIWeather> {
    override fun get(): APIWeather {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIWeather::class.java)
    }
}
