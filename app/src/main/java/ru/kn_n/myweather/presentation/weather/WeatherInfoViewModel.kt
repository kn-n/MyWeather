package ru.kn_n.myweather.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.kn_n.myweather.data.repositories.MainRepository
import ru.kn_n.myweather.utils.Resource
import javax.inject.Inject

class WeatherInfoViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

    fun getWeather(lat: String, lon: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getWeather(lat, lon)))
        } catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}