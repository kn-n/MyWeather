package ru.kn_n.myweather.presentation.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kn_n.myweather.databinding.ItemHourlyWeatherForecastBinding
import ru.kn_n.myweather.domain.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.utils.WeatherIconCode

class HourlyWeatherForecastAdapter(private val data: List<HourlyWeatherForecastEntity>) :
    RecyclerView.Adapter<HourlyWeatherForecastAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHourlyWeatherForecastBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHourlyWeatherForecastBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.time.text = item.time
        holder.binding.temp.text = item.temperature
        Log.d("ENUM", item.weatherCode)
        holder.binding.icWeather.setImageResource(WeatherIconCode.valueOf(item.weatherCode).resId)
    }

    override fun getItemCount() = data.size
}