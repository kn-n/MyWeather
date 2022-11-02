package ru.kn_n.myweather.data.repositories

import ru.kn_n.myweather.data.api.APIHelper
import ru.kn_n.myweather.data.api.Geocoder
import ru.kn_n.myweather.data.mappers.GeocodingResponseMapper
import ru.kn_n.myweather.data.model.GeocodingResponse
import javax.inject.Inject

class PlacesRepository @Inject constructor(private val geocoder: Geocoder, private val apiHelper: APIHelper) {
    suspend fun getPlaces(name: String) = GeocodingResponseMapper().mapGeocodingResponse(apiHelper.getPlaces(name))

    fun getPlace(lat: String, lon: String) = GeocodingResponseMapper().mapGeocoderResponse(geocoder.getPlace(lat, lon)[0])
}