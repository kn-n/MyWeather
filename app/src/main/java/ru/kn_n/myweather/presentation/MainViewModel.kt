package ru.kn_n.myweather.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ru.kn_n.myweather.presentation.navigation.Screens
import ru.kn_n.myweather.utils.Constants
import ru.kn_n.myweather.utils.Constants.BASE_LAT
import ru.kn_n.myweather.utils.Constants.BASE_LON
import javax.inject.Inject

class MainViewModel @Inject constructor(private var router: Router) : ViewModel() {

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
            Log.d("GL", location.toString())
            router.navigateTo(Screens.WeatherInfo(location.latitude.toString(), location.longitude.toString()))
        }
    }

    private fun navigateWithBaseLocationToWeatherInfoFragment() {
        router.navigateTo(Screens.WeatherInfo(BASE_LAT, BASE_LON))
    }
}