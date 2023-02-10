package ru.kn_n.myweather.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kn_n.myweather.data.model.GeocodingResponse

interface APIPlace {
    @GET("search")
    suspend fun getPlaces(
        @Query("name") name: String,
        @Query("language") language: String = "ru"
    ): GeocodingResponse
}