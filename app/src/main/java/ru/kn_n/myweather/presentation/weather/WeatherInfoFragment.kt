package ru.kn_n.myweather.presentation.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.kn_n.myweather.databinding.FragmentWeatherInfoBinding
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.entities.ForecastEntity
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.presentation.ViewModelFactory
import ru.kn_n.myweather.utils.Status
import toothpick.Toothpick.openScope
import javax.inject.Inject

class WeatherInfoFragment : Fragment() {

    @Inject
    lateinit var viewModel: WeatherInfoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentWeatherInfoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWeatherInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()
        setupObservers()
        setupAdapter()

        return root
    }

    private fun setupViewModel() {
        viewModelFactory = openScope(Scopes.APP_SCOPE).getInstance(ViewModelFactory::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherInfoViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.getWeather("56.897699", "60.626907").observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data!!
                        Log.d("DEG", data.currentForecast.temp)
                        showCurrentWeatherForecast(data.currentForecast)
                        showHourlyWeatherForecast(data.hourlyForecast)
                    }
                    Status.ERROR -> {
                        Log.d("DEG", it.message.toString())
                    }
                    Status.LOADING -> {
                        Log.d("DEG", "loading")
                    }
                }
            }
        })
    }

    private fun setupAdapter() {
        binding.hourlyWeatherForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    @SuppressLint("SetTextI18n")
    private fun showCurrentWeatherForecast(data: CurrentWeatherForecastEntity) {
        with(binding) {
            temperatureNow.text = data.temp
            feelsLikeDegrees.text = data.feelsLike
            weatherStatus.text = data.weather[0].description
            wind.text = data.windSpeed
            direction.text = data.windDeg
            humidity.text = data.humidity
            pressure.text = data.pressure
            uv.text = data.uvi
        }
    }

    private fun showHourlyWeatherForecast(data: List<HourlyWeatherForecastEntity>) {
        binding.hourlyWeatherForecastRecyclerView.adapter = HourlyWeatherForecastAdapter(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): WeatherInfoFragment {
            return WeatherInfoFragment()
        }
    }
}