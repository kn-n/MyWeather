package ru.kn_n.myweather.presentation.weather

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.terrakok.cicerone.Router
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import ru.kn_n.myweather.data.repositories.PlacesRepository
import ru.kn_n.myweather.data.repositories.WeatherInfoRepository
import ru.kn_n.myweather.presentation.cache.Cache
import ru.kn_n.myweather.presentation.navigation.Screens
import ru.kn_n.myweather.utils.Resource
import ru.kn_n.myweather.utils.haveLocationPermissions
import javax.inject.Inject

class WeatherInfoViewModel @Inject constructor(
    private val router: Router,
    private val weatherInfoRepository: WeatherInfoRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun getWeather(lat: String, lon: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = weatherInfoRepository.getWeather(lat, lon)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

//    @SuppressLint("MissingPermission")
//    fun getLocation(context: Context){
//        if (haveLocationPermissions(context)) {
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//            fusedLocationClient.lastLocation.addOnCompleteListener { }
//        } else {
//        }
//    }

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