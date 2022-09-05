package ru.kn_n.myweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.presentation.weather.WeatherInfoViewModel
import toothpick.Toothpick
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor() :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(WeatherInfoViewModel::class.java)) {
            return Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.WEATHER_INFO_SCOPE).getInstance(modelClass) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}