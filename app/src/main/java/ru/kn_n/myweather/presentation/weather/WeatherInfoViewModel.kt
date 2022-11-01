package ru.kn_n.myweather.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import ru.kn_n.myweather.data.repositories.PlacesRepository
import ru.kn_n.myweather.data.repositories.WeatherInfoRepository
import ru.kn_n.myweather.presentation.navigation.Screens
import ru.kn_n.myweather.utils.Resource
import javax.inject.Inject

class WeatherInfoViewModel @Inject constructor(
    private val router: Router,
    private val weatherInfoRepository: WeatherInfoRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    fun getWeather(lat: String, lon: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = weatherInfoRepository.getWeather(lat, lon)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getPlace(lat: String, lon: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = placesRepository.getPlace(lat, lon)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun routToChooseFragment() {
        router.navigateTo(Screens.ChoosePlace())
    }

}