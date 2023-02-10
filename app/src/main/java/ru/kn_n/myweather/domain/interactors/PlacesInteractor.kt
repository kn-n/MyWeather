package ru.kn_n.myweather.domain.interactors

import ru.kn_n.myweather.data.repositories.PlacesRepository
import javax.inject.Inject

class PlacesInteractor @Inject constructor(
    private val placesRepository: PlacesRepository
){
    fun getPlace(lat: String, lon: String) = placesRepository.getPlace(lat, lon)

    suspend fun getPlaces(name: String) = placesRepository.getPlaces(name)
}