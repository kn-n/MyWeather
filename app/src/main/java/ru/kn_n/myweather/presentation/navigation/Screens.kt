package ru.kn_n.myweather.presentation.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.kn_n.myweather.presentation.choosePlace.ChoosePlaceFragment
import ru.kn_n.myweather.presentation.weather.WeatherInfoFragment

object Screens {
    fun WeatherInfo() = FragmentScreen { WeatherInfoFragment() }

    fun ChoosePlace() = FragmentScreen { ChoosePlaceFragment() }
}