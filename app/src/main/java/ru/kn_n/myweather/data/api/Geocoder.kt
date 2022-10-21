package ru.kn_n.myweather.data.api

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*
import javax.inject.Inject

class Geocoder @Inject constructor(context: Context) {
    private val geocoder = Geocoder(context, Locale.getDefault())
    
    fun getPlaces(query: String): MutableList<Address> = geocoder.getFromLocationName(query, 5)
}