package ru.kn_n.myweather.presentation.choosePlace

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.terrakok.cicerone.Router
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import ru.kn_n.myweather.data.repositories.PlacesRepository
import ru.kn_n.myweather.entities.GeocodingEntity
import ru.kn_n.myweather.presentation.navigation.Screens
import ru.kn_n.myweather.utils.Constants
import ru.kn_n.myweather.utils.Resource
import javax.inject.Inject

class ChoosePlaceViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val router: Router
) : ViewModel() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray, context: Context) {
        if (requestCode == Constants.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateWithLocationToWeatherInfoFragment(context)
            }
        }
    }

    fun search(name: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = placesRepository.getPlaces(name)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

//    fun startSearch(search: SearchView) {
//        search.setOnQueryTextListener(
//            object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(text: String?): Boolean {
//                    if (text != null) {
//                        search(text)
//                    } else {
//
//                    }
//                    return false
//                }
//
//                override fun onQueryTextChange(query: String?): Boolean {
//                    if (query != null) {
//                        search(query)
//                    }
//                    return false
//                }
//            }
//        )
//    }

    fun navigateWithPlaceToWeather(lat: String, lon: String) {
        router.navigateTo(Screens.WeatherInfo(lat, lon))
    }

    @SuppressLint("MissingPermission")
    fun navigateWithLocationToWeatherInfoFragment(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            Log.d("GL", location.toString())
            router.navigateTo(Screens.WeatherInfo(location.latitude.toString(), location.longitude.toString()))
        }
    }
}