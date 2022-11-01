package ru.kn_n.myweather.data.model

class GeocodingResponse(
    val results: List<PlaceResponse>
)

class PlaceResponse(
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val country: String?,
    val admin1: String?
)