package ru.kn_n.myweather.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun dateTime(time: Long, zone: String, format: String = "HH:mm"): String {
    val zoneId = ZoneId.of(zone)
    val instant = Instant.ofEpochSecond(time)
    val formatter = DateTimeFormatter.ofPattern(format)
    return instant.atZone(zoneId).format(formatter)
}