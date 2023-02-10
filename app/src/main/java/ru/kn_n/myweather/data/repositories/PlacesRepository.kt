package ru.kn_n.myweather.data.repositories

import ru.kn_n.myweather.data.api.APIPlace
import ru.kn_n.myweather.data.api.Geocoder
import ru.kn_n.myweather.data.mappers.GeocodingResponseMapper
import javax.inject.Inject

class PlacesRepository @Inject constructor(private val geocoder: Geocoder, private val apiPlace: APIPlace) {
    suspend fun getPlaces(name: String) = GeocodingResponseMapper().mapGeocodingResponse(apiPlace.getPlaces(name))

    fun getPlace(lat: String, lon: String) =
        GeocodingResponseMapper().mapGeocoderResponse(geocoder.getPlace(lat, lon)[0])
}