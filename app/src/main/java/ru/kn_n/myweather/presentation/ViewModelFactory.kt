package ru.kn_n.myweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.presentation.choosePlace.ChoosePlaceViewModel
import ru.kn_n.myweather.presentation.weather.WeatherInfoViewModel
import toothpick.Toothpick
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor() :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                Toothpick.openScope(Scopes.APP_SCOPE).getInstance(modelClass) as T
            modelClass.isAssignableFrom(WeatherInfoViewModel::class.java) ->
                Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.WEATHER_INFO_SCOPE).getInstance(modelClass) as T
            modelClass.isAssignableFrom(ChoosePlaceViewModel::class.java) ->
                Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.CHOOSE_PLACE_SCOPE).getInstance(modelClass) as T
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}