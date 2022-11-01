package ru.kn_n.myweather.data.mappers

import android.util.Log
import ru.kn_n.myweather.data.model.HourlyWeatherResponse
import ru.kn_n.myweather.data.model.WeatherResponse
import ru.kn_n.myweather.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.entities.ForecastEntity
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.utils.EMPTY
import ru.kn_n.myweather.utils.dateTime
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class WeatherResponseMapper {
    fun mapWeatherResponse(response: WeatherResponse): ForecastEntity {
        return ForecastEntity(
            currentForecast = mapCurrentWeatherResponse(response),
            hourlyForecast = mapHourlyWeatherResponse(response.hourly)
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
                                    currentWeather.time == null)
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

    private fun mapHourlyWeatherResponse(response: HourlyWeatherResponse): List<HourlyWeatherForecastEntity> {
        val timeList = if (response.time.isNullOrEmpty()) emptyList() else response.time.map { dateTime(it) }
        val temperatureList = if (response.temperature_2m.isNullOrEmpty()) emptyList()
                                else response.temperature_2m.map { getTemp(it) }
        val weatherCodeList = if (response.weathercode.isNullOrEmpty()) emptyList() else response.weathercode
        val result: MutableList<HourlyWeatherForecastEntity> = mutableListOf()
        if (timeList.isNotEmpty()
            && temperatureList.isNotEmpty()
            && weatherCodeList.isNotEmpty()
        ) {
            for (i in timeList) {
                val index = timeList.indexOf(i)
                result.add(
                    HourlyWeatherForecastEntity(
                        time = i,
                        temperature = temperatureList[index],
                        weatherCode = weatherCodeList[index].toString()
                    )
                )
            }
            return result
        } else return emptyList()
    }

    private fun getWindSpeed(speed: Double): String {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(speed*0.28)
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
        val NORTH = "С"
        val NORTHWEST = "СЗ"
        val NORTHEAST = "СВ"
        val SOUTH = "Ю"
        val SOUTHWEST = "ЮЗ"
        val SOUTHEAST = "ЮВ"
        val EAST = "В"
        val WEST = "З"
        val CLEAR_SKY = "Ясно"
        val MAINLY_CLEAR = "Переменная облачность"
        val PARTLY_CLOUDY = "Облачно"
        val OVERCAST = "Пасмурно"
        val FOG = "Туман"
        val DEP_RIME_FOG = "Иней"
        val DRIZZLE_LIGHT = "Легкий моросящий дождь"
        val DRIZZLE_MODERATE = "Моросящий дождь"
        val DRIZZLE_INTENSIVE = "Сильный моросящий дождь"
        val FREEZING_DRIZZLE_LIGHT = "Гололед"
        val FREEZING_DRIZZLE_INTENSIVE = "Гололед"
        val RAIN_LIGHT = "Легкий дождь"
        val RAIN_MODERATE = "Дождь"
        val RAIN_INTENSIVE = "Сильный дождь"
        val FREEZING_RAIN_LIGHT = "Легкий ледяной дождь"
        val FREEZING_RAIN_INTENSIVE = "Сильный ледяной дождь"
        val SNOW_FALL_LIGHT = "Легкий снегопад"
        val SNOW_FALL_MODERATE = "Снегопад"
        val SNOW_FALL_INTENSIVE = "Сильный снегопад"
        val SNOW_GRAINS = "Снежные зерна"
        val RAIN_SHOWERS_LIGHT = "Слабый ливень"
        val RAIN_SHOWERS_MODERATE = "Ливень"
        val RAIN_SHOWERS_INTENSIVE = "Сильный ливень"
        val SNOW_SHOWERS_LIGHT = "Слабая метель"
        val SNOW_SHOWERS_INTENSIVE = "Сильная метель"

    }
}