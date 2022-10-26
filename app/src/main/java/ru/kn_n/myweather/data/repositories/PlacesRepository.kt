package ru.kn_n.myweather.data.repositories

import ru.kn_n.myweather.data.api.APIHelper
import ru.kn_n.myweather.data.api.Geocoder
import javax.inject.Inject

class PlacesRepository @Inject constructor(private val geocoder: Geocoder) {
    fun getPlaces(query: String) = geocoder.getPlaces(query)

    fun getPlace(lat: String, lon: String) = geocoder.getPlace(lat, lon)
}