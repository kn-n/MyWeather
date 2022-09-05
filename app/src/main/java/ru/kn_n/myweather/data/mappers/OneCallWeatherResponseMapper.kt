package ru.kn_n.myweather.data.mappers

import ru.kn_n.myweather.data.model.CurrentWeatherResponse
import ru.kn_n.myweather.data.model.HourlyWeatherResponse
import ru.kn_n.myweather.data.model.OneCallWeatherResponse
import ru.kn_n.myweather.data.model.WeatherResponse
import ru.kn_n.myweather.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.entities.ForecastEntity
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.entities.WeatherEntity
import ru.kn_n.myweather.utils.dateTime
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class OneCallWeatherResponseMapper {

    lateinit var timeZone: String

    fun mapOneCallWeatherResponse(response: OneCallWeatherResponse): ForecastEntity {
        timeZone = response.timezone?:""
        return ForecastEntity(
            currentForecast = mapCurrentWeatherResponse(response.current),
            hourlyForecast = response.hourly?.map { mapHourlyWeatherResponse(it) }.orEmpty()
        )
    }

    private fun mapWeatherResponse(response: WeatherResponse): WeatherEntity {
        return WeatherEntity(
            description = response.description?.substring(0, 1)?.uppercase() +
                response.description?.substring(1, response.description.length),
            iconId = response.icon.orEmpty()
        )
    }

    private fun mapHourlyWeatherResponse(response: HourlyWeatherResponse): HourlyWeatherForecastEntity {
        return HourlyWeatherForecastEntity(
            time = getTime(response.dt?:0),
            temp = getPlusTemp(response.temp?.roundToInt().toString()),
            weather = response.weather?.map { mapWeatherResponse(it) }.orEmpty()
        )
    }

    private fun mapCurrentWeatherResponse(response: CurrentWeatherResponse): CurrentWeatherForecastEntity {
        return CurrentWeatherForecastEntity(
            temp = getPlusTemp(response.temp?.roundToInt().toString()),
            feelsLike = getPlusTemp(response.feels_like?.roundToInt().toString()),
            pressure = response.pressure?.times(0.75006)?.roundToInt().toString(),
            humidity = response.humidity.toString(),
            uvi = response.uvi?.roundToInt().toString(),
            windSpeed = "%.1f".format(response.wind_speed ?: -1.0).toDouble().toString(),
            windDeg = getDirection(response.wind_deg ?: -1),
//            windDeg = response.wind_deg.toString(),
            weather = response.weather?.map { mapWeatherResponse(it) }.orEmpty()
        )
    }

    private fun getTime(time: Long) =
        if (time.toInt() == 0) {
            ""
        } else {
            dateTime(time, timeZone)
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

    private fun getPlusTemp(temp: String) =
        if (temp.toInt() > 0) {
            "+${temp}"
        } else {
            temp
        }

    companion object {
        val NORTH = "С"
        val NORTHWEST = "СЗ"
        val NORTHEAST = "СВ"
        val SOUTH = "Ю"
        val SOUTHWEST = "ЮЗ"
        val SOUTHEAST = "ЮВ"
        val EAST = "В"
        val WEST = "З"
    }
}