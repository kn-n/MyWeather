package ru.kn_n.myweather.presentation.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kn_n.myweather.R
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity

class HourlyWeatherForecastAdapter(private val data: List<HourlyWeatherForecastEntity>):
    RecyclerView.Adapter<HourlyWeatherForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_weather_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.time.text = item.time
        holder.degrees.text = item.temp
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.time)
        val icon: ImageView = itemView.findViewById(R.id.ic_weather)
        val degrees: TextView = itemView.findViewById(R.id.temp)
    }
}