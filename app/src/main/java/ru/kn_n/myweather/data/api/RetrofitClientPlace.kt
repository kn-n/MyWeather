package ru.kn_n.myweather.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kn_n.myweather.utils.Constants
import javax.inject.Inject
import javax.inject.Provider

class RetrofitClientPlace @Inject constructor(): Provider<APIPlace>{
    override fun get(): APIPlace {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_GEOCODING)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIPlace::class.java)
    }
}