package ru.kn_n.myweather.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.github.rahatarmanahmed.cpv.CircularProgressView
import ru.kn_n.myweather.domain.entities.PlaceEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun dateTime(time: String, patternFrom: String, patternTo: String): String {
    val newTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(patternFrom))
    val t = newTime.format(DateTimeFormatter.ofPattern(patternTo))
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

fun startCircularLoading(loading: CircularProgressView) {
    loading.show()
    loading.startAnimation()
}

fun stopCircularLoading(loading: CircularProgressView) {
    loading.stopAnimation()
    loading.gone()
}

fun showErrorText(error: TextView) {
    error.show()
}

fun hiddenErrorText(error: TextView) {
    error.gone()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun makePlaceName(place: PlaceEntity): String{
    val list = mutableListOf(place.country, place.admin, place.name)
    list.removeIf { it.isEmpty() }
    return list.joinToString(",")
}