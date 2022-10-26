package ru.kn_n.myweather.presentation.navigation

import android.location.Address
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.kn_n.myweather.presentation.choosePlace.ChoosePlaceFragment
import ru.kn_n.myweather.presentation.weather.WeatherInfoFragment

object Screens {
    fun WeatherInfo(lat:String, lon:String) = FragmentScreen { WeatherInfoFragment.instance(lat,lon) }
    fun ChoosePlace() = FragmentScreen { ChoosePlaceFragment() }
}