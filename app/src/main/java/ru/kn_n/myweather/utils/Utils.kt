package ru.kn_n.myweather.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun dateTime(time: String): String {
    val newTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
    val t = newTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    return t.toString()
}

fun haveLocationPermissions(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED)
}

val String.Companion.EMPTY: String
    get() = ""