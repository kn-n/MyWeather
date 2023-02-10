package ru.kn_n.myweather.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ru.kn_n.myweather.presentation.model.LocationCache
import ru.kn_n.myweather.presentation.navigation.Screens
import ru.kn_n.myweather.utils.Constants
import ru.kn_n.myweather.utils.Constants.BASE_LAT
import ru.kn_n.myweather.utils.Constants.BASE_LON
import ru.kn_n.myweather.utils.EMPTY
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router,
    private val cache: LocationCache
) : ViewModel() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray, context: Context) {
        if (requestCode == Constants.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateWithLocationToWeatherInfoFragment(context)
            } else {
                navigateWithBaseLocationToWeatherInfoFragment()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun navigateWithLocationToWeatherInfoFragment(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            cache.placeLat = location.latitude.toString()
            cache.placeLon = location.longitude.toString()
            cache.placeName = String.EMPTY
            router.navigateTo(Screens.WeatherInfo())
        }
    }

    private fun navigateWithBaseLocationToWeatherInfoFragment() {
        cache.placeLat = BASE_LAT
        cache.placeLon = BASE_LON
        cache.placeName = String.EMPTY
        router.navigateTo(Screens.WeatherInfo())
    }
}