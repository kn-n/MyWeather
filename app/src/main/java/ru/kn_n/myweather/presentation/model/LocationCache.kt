package ru.kn_n.myweather.presentation.model

import javax.inject.Inject

class LocationCache @Inject constructor(){
    var placeLat = ""
    var placeLon = ""
    var placeName = ""
}