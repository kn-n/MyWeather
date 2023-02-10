package ru.kn_n.myweather.data.mappers

import ru.kn_n.myweather.data.model.WeatherResponse
import ru.kn_n.myweather.domain.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.domain.entities.ForecastEntity
import ru.kn_n.myweather.domain.entities.HourlyWeatherForecastEntity
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
            weatherCode = currentWeather.weathercode?.let { "CODE_${currentWeather.weathercode}" } ?: String.EMPTY,
            weatherDescription = currentWeather.weathercode?.let {
                getDescription(currentWeather.weathercode)
            } ?: String.EMPTY,
            temperature = currentWeather.temperature?.let { getTemp(currentWeather.temperature) } ?: String.EMPTY,
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
            windSpeed = currentWeather.windspeed?.let { getWindSpeed(currentWeather.windspeed) } ?: String.EMPTY,
            windDirection = currentWeather.winddirection?.let {
                getDirection(currentWeather.winddirection.roundToInt())
            } ?: String.EMPTY,
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
        val timeList = if (hourlyWeather.time.isNullOrEmpty()) emptyList() else hourlyWeather.time.map {
            dateTime(
                it,
                ISO_LOCAL_DATE_TIME_SHORT,
                ISO_LOCAL_TIME_SHORT
            )
        }
        val temperatureList = if (hourlyWeather.temperature_2m.isNullOrEmpty()) emptyList()
        else hourlyWeather.temperature_2m.map { getTemp(it) }
        val weatherCodeList = if (hourlyWeather.weathercode.isNullOrEmpty()) emptyList()
        else getStringWeatherCodeList(hourlyWeather.weathercode)
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

    private fun getStringWeatherCodeList(intList: List<Int>): List<String> {
        val stringList = mutableListOf<String>()
        intList.map { stringList.add("CODE_$it") }
        return stringList
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
        const val NORTH = "С"
        const val NORTHWEST = "СЗ"
        const val NORTHEAST = "СВ"
        const val SOUTH = "Ю"
        const val SOUTHWEST = "ЮЗ"
        const val SOUTHEAST = "ЮВ"
        const val EAST = "В"
        const val WEST = "З"
        const val CLEAR_SKY = "Ясно"
        const val MAINLY_CLEAR = "Переменная облачность"
        const val PARTLY_CLOUDY = "Облачно"
        const val OVERCAST = "Пасмурно"
        const val FOG = "Туман"
        const val DEP_RIME_FOG = "Иней"
        const val DRIZZLE_LIGHT = "Легкий моросящий дождь"
        const val DRIZZLE_MODERATE = "Моросящий дождь"
        const val DRIZZLE_INTENSIVE = "Сильный моросящий дождь"
        const val FREEZING_DRIZZLE_LIGHT = "Гололед"
        const val FREEZING_DRIZZLE_INTENSIVE = "Гололед"
        const val RAIN_LIGHT = "Легкий дождь"
        const val RAIN_MODERATE = "Дождь"
        const val RAIN_INTENSIVE = "Сильный дождь"
        const val FREEZING_RAIN_LIGHT = "Легкий град"
        const val FREEZING_RAIN_INTENSIVE = "Сильный град"
        const val SNOW_FALL_LIGHT = "Легкий снегопад"
        const val SNOW_FALL_MODERATE = "Снегопад"
        const val SNOW_FALL_INTENSIVE = "Сильный снегопад"
        const val SNOW_GRAINS = "Снегопад"
        const val RAIN_SHOWERS_LIGHT = "Слабый ливень"
        const val RAIN_SHOWERS_MODERATE = "Ливень"
        const val RAIN_SHOWERS_INTENSIVE = "Сильный ливень"
        const val SNOW_SHOWERS_LIGHT = "Слабая метель"
        const val SNOW_SHOWERS_INTENSIVE = "Сильная метель"
    }
}