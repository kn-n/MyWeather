package ru.kn_n.myweather.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import ru.kn_n.myweather.presentation.cache.Cache
import ru.kn_n.myweather.utils.Constants
import ru.kn_n.myweather.utils.requestPermission
import javax.inject.Inject

class MainViewModel @Inject constructor(private var cache: Cache): ViewModel() {

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray){
        if (requestCode == Constants.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cache.haveLocationPermissions = true
            }
        }
    }

    fun checkLocationPermissions(context: Context){
        if (ru.kn_n.myweather.utils.checkLocationPermissions(context)){
            requestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }
}