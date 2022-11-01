package ru.kn_n.myweather.presentation.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kn_n.myweather.databinding.ItemHourlyWeatherForecastBinding
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity

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
    }

    override fun getItemCount() = data.size
}