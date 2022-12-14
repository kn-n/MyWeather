package ru.kn_n.myweather.data.mappers

import android.util.Log
import ru.kn_n.myweather.data.model.WeatherResponse
import ru.kn_n.myweather.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.entities.ForecastEntity
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.utils.Constants.ISO_LOCAL_DATE_TIME_SHORT
import ru.kn_n.myweather.utils.Constants.ISO_LOCAL_TIME_SHORT
import ru.kn_n.myweather.utils.EMPTY
import ru.kn_n.myweather.utils.dateTime
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class WeatherResponseMapper {
    fun mapWeatherResponse(response: WeatherResponse): ForecastEntity {
        return ForecastEntity(
            currentForecast = mapCurrentWeatherResponse(response),
            hourlyForecast = mapHourlyWeatherResponse(response)
        )
    }

    private fun mapCurrentWeatherResponse(response: WeatherResponse): CurrentWeatherForecastEntity {
        val currentWeather = response.current_weather
        val hourlyWeather = response.hourly
        return CurrentWeatherForecastEntity(
            weatherCode = currentWeather.weathercode.toString().takeIf { currentWeather.weathercode != null }.orEmpty(),
            weatherDescription = if (currentWeather.weathercode == null) String.EMPTY
            else getDescription(currentWeather.weathercode),
            temperature = if (currentWeather.temperature == null) String.EMPTY
            else getTemp(currentWeather.temperature),
            feelTemperature = if (hourlyWeather.time == null ||
                hourlyWeather.apparent_temperature == null ||
                currentWeather.time == null
            )
                String.EMPTY
            else getFeelTemp(
                currentWeather.time,
                hourlyWeather.time,
                hourlyWeather.apparent_temperature
            ),
            windSpeed = if (currentWeather.windspeed == null) String.EMPTY
            else getWindSpeed(currentWeather.windspeed),
            windDirection = if (currentWeather.winddirection == null) String.EMPTY
            else getDirection(currentWeather.winddirection.roundToInt()),
            pressure = if (hourlyWeather.time == null ||
                hourlyWeather.surface_pressure == null ||
                currentWeather.time == null
            ) String.EMPTY
            else getPressure(
                currentWeather.time,
                hourlyWeather.time,
                hourlyWeather.surface_pressure
            ),
            humidity = if (hourlyWeather.time == null ||
                hourlyWeather.relativehumidity_2m == null ||
                currentWeather.time == null
            ) String.EMPTY
            else getHumidity(
                currentWeather.time,
                hourlyWeather.time,
                hourlyWeather.relativehumidity_2m
            )
        )
    }

    private fun mapHourlyWeatherResponse(response: WeatherResponse): List<HourlyWeatherForecastEntity> {
        val currentWeather = response.current_weather
        val hourlyWeather = response.hourly
        val firstIndex = if (!hourlyWeather.time.isNullOrEmpty()) hourlyWeather.time.indexOf(currentWeather.time) else 0
        val timeList = if (hourlyWeather.time.isNullOrEmpty()) emptyList() else hourlyWeather.time.map { dateTime(it, ISO_LOCAL_DATE_TIME_SHORT, ISO_LOCAL_TIME_SHORT) }
        val temperatureList = if (hourlyWeather.temperature_2m.isNullOrEmpty()) emptyList()
        else hourlyWeather.temperature_2m.map { getTemp(it) }
        val weatherCodeList = if (hourlyWeather.weathercode.isNullOrEmpty()) emptyList() else hourlyWeather.weathercode
        val result: MutableList<HourlyWeatherForecastEntity> = mutableListOf()
        return if (timeList.isNotEmpty()
            && temperatureList.isNotEmpty()
            && weatherCodeList.isNotEmpty()
        ) {
            for (i in firstIndex until timeList.size) {
                result.add(
                    HourlyWeatherForecastEntity(
                        time = timeList[i],
                        temperature = temperatureList[i],
                        weatherCode = weatherCodeList[i].toString()
                    )
                )
            }
            result
        } else emptyList()
    }

    private fun getWindSpeed(speed: Double): String {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(speed * 0.28)
    }

    private fun getDirection(deg: Int) =
        when (deg) {
            in 0..10 -> NORTH
            in 350..360 -> NORTH
            in 11..80 -> NORTHEAST
            in 81..100 -> EAST
            in 101..170 -> SOUTHEAST
            in 171..190 -> SOUTH
            in 191..260 -> SOUTHWEST
            in 261..280 -> WEST
            in 281..349 -> NORTHWEST
            else -> {
                ""
            }
        }

    private fun getFeelTemp(time: String, timeList: List<String>, tempList: List<Double>): String =
        getTemp(tempList[timeList.indexOf(time)])

    private fun getTemp(temp: Double): String {
        val t = temp.roundToInt().toString()
        return if (t.contains("-")) t
        else "+${t}"
    }

    private fun getPressure(time: String, timeList: List<String>, pressureList: List<Double>): String =
        (pressureList[timeList.indexOf(time)] * 0.75).roundToInt().toString()

    private fun getHumidity(time: String, timeList: List<String>, humidityList: List<Int>): String =
        humidityList[timeList.indexOf(time)].toString()

    private fun getDescription(code: Int) =
        when (code) {
            0 -> CLEAR_SKY
            1 -> MAINLY_CLEAR
            2 -> PARTLY_CLOUDY
            3 -> OVERCAST
            45 -> FOG
            48 -> DEP_RIME_FOG
            51 -> DRIZZLE_LIGHT
            53 -> DRIZZLE_MODERATE
            55 -> DRIZZLE_INTENSIVE
            56 -> FREEZING_DRIZZLE_LIGHT
            57 -> FREEZING_DRIZZLE_INTENSIVE
            61 -> RAIN_LIGHT
            63 -> RAIN_MODERATE
            65 -> RAIN_INTENSIVE
            66 -> FREEZING_RAIN_LIGHT
            67 -> FREEZING_RAIN_INTENSIVE
            71 -> SNOW_FALL_LIGHT
            73 -> SNOW_FALL_MODERATE
            75 -> SNOW_FALL_INTENSIVE
            77 -> SNOW_GRAINS
            80 -> RAIN_SHOWERS_LIGHT
            81 -> RAIN_SHOWERS_MODERATE
            82 -> RAIN_SHOWERS_INTENSIVE
            85 -> SNOW_SHOWERS_LIGHT
            86 -> SNOW_SHOWERS_INTENSIVE
            else -> {
                ""
            }
        }

    companion object {
        val NORTH = "??"
        val NORTHWEST = "????"
        val NORTHEAST = "????"
        val SOUTH = "??"
        val SOUTHWEST = "????"
        val SOUTHEAST = "????"
        val EAST = "??"
        val WEST = "??"
        val CLEAR_SKY = "????????"
        val MAINLY_CLEAR = "???????????????????? ????????????????????"
        val PARTLY_CLOUDY = "??????????????"
        val OVERCAST = "????????????????"
        val FOG = "??????????"
        val DEP_RIME_FOG = "????????"
        val DRIZZLE_LIGHT = "???????????? ?????????????????? ??????????"
        val DRIZZLE_MODERATE = "?????????????????? ??????????"
        val DRIZZLE_INTENSIVE = "?????????????? ?????????????????? ??????????"
        val FREEZING_DRIZZLE_LIGHT = "??????????????"
        val FREEZING_DRIZZLE_INTENSIVE = "??????????????"
        val RAIN_LIGHT = "???????????? ??????????"
        val RAIN_MODERATE = "??????????"
        val RAIN_INTENSIVE = "?????????????? ??????????"
        val FREEZING_RAIN_LIGHT = "???????????? ?????????????? ??????????"
        val FREEZING_RAIN_INTENSIVE = "?????????????? ?????????????? ??????????"
        val SNOW_FALL_LIGHT = "???????????? ????????????????"
        val SNOW_FALL_MODERATE = "????????????????"
        val SNOW_FALL_INTENSIVE = "?????????????? ????????????????"
        val SNOW_GRAINS = "?????????????? ??????????"
        val RAIN_SHOWERS_LIGHT = "???????????? ????????????"
        val RAIN_SHOWERS_MODERATE = "????????????"
        val RAIN_SHOWERS_INTENSIVE = "?????????????? ????????????"
        val SNOW_SHOWERS_LIGHT = "???????????? ????????????"
        val SNOW_SHOWERS_INTENSIVE = "?????????????? ????????????"

    }
}