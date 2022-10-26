package ru.kn_n.myweather.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import ru.kn_n.myweather.presentation.MainActivity
import ru.kn_n.myweather.utils.Constants.PERMISSION_ID
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun dateTime(time: Long, zone: String, format: String = "HH:mm"): String {
    val zoneId = ZoneId.of(zone)
    val instant = Instant.ofEpochSecond(time)
    val formatter = DateTimeFormatter.ofPattern(format)
    return instant.atZone(zoneId).format(formatter)
}

fun checkLocationPermissions(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED)
}

fun requestPermission(permissions: Array<String>){
    ActivityCompat.requestPermissions(
        MainActivity(),
        permissions,
        PERMISSION_ID
    )
}