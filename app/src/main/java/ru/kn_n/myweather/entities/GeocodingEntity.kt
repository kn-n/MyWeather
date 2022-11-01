package ru.kn_n.myweather.entities

class GeocodingEntity (
    val listOfPlaces: List<PlaceEntity>
)

class PlaceEntity(
    val name: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val country: String = "",
    val admin: String = ""
)