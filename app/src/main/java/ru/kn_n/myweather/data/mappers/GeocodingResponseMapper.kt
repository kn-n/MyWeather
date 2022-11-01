package ru.kn_n.myweather.data.mappers

import ru.kn_n.myweather.data.model.GeocodingResponse
import ru.kn_n.myweather.data.model.PlaceResponse
import ru.kn_n.myweather.entities.GeocodingEntity
import ru.kn_n.myweather.entities.PlaceEntity

class GeocodingResponseMapper {
    fun mapGeocodingResponse(response: GeocodingResponse): GeocodingEntity {
        return GeocodingEntity(
            listOfPlaces = response.results.map { mapPlaceResponse(it) }
        )
    }

    private fun mapPlaceResponse(response: PlaceResponse): PlaceEntity {
        return PlaceEntity(
            name = response.name.toString().takeIf { response.name != null }.orEmpty(),
            latitude = response.latitude.toString().takeIf { response.latitude != null }.orEmpty(),
            longitude = response.longitude.toString().takeIf { response.longitude != null }.orEmpty(),
            country = response.country.toString().takeIf { response.country != null }.orEmpty(),
            admin = response.admin1.toString().takeIf { response.admin1 != null }.orEmpty()
        )
    }
}