package ru.kn_n.myweather.presentation.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetferrytan.skeletonplaceholderview.RectBone
import ru.kn_n.myweather.R
import ru.kn_n.myweather.databinding.FragmentWeatherInfoBinding
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.domain.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.domain.entities.ForecastEntity
import ru.kn_n.myweather.domain.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.presentation.ViewModelFactory
import ru.kn_n.myweather.presentation.base.BaseFragment
import ru.kn_n.myweather.utils.Resource
import ru.kn_n.myweather.utils.WeatherIconCode
import ru.kn_n.myweather.utils.gone
import ru.kn_n.myweather.utils.show
import toothpick.Toothpick.openScope
import javax.inject.Inject

class WeatherInfoFragment : BaseFragment<FragmentWeatherInfoBinding>(FragmentWeatherInfoBinding::inflate) {

    @Inject
    lateinit var viewModel: WeatherInfoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()
        setUpSkeleton()
        setUpPlace()

        binding.placeContainer.setOnClickListener {
            viewModel.routToChooseFragment()
        }

        binding.update.setOnClickListener {
            getWeather()
        }
    }

    private fun setUpPlace() {
        viewModel.initCache()
        viewModel.place.observe(viewLifecycleOwner) {
            showPlace(it)
        }
    }

    override fun onResume() {
        super.onResume()
        getWeather()
    }

    private fun setupViewModel() {
        viewModelFactory = openScope(Scopes.APP_SCOPE).getInstance(ViewModelFactory::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherInfoViewModel::class.java]
    }

    private fun getWeather() {
        viewModel.getWeather()
        viewModel.resultWeather.observe(viewLifecycleOwner) {
            showRequestResult(
                resource = it,
                doOnSuccess = { it.data?.let { data -> showForecast(data) } },
                doOnLoading = { showLoading() },
                doOnError = { showError(it) }
            )
        }
    }

    private fun showForecast(data: ForecastEntity) {
        stopSkeleton()
        showCurrentWeatherForecast(data.currentForecast)
        showHourlyWeatherForecast(data.hourlyForecast)
    }

    private fun showError(data: Resource<ForecastEntity>) {
        stopSkeleton()
        Log.d("WF", data.message.toString())
    }

    private fun showLoading() {
        showSkeleton()
        Log.d("WF", "loading")
    }

    private fun setUpSkeleton() {
        binding.skeleton.skeletonPlaceholderView.skinView(
            R.layout.fragment_weather_info,
            RectBone(R.id.weather_card),
            RectBone(R.id.weather_params),
            RectBone(R.id.place_container)
        )
        binding.skeleton.shimmer.startShimmer()
    }

    private fun showSkeleton() {
        binding.skeleton.skeletonPlaceholderView.show()
    }

    private fun stopSkeleton() {
        binding.skeleton.skeletonPlaceholderView.gone()
    }

    private fun setupAdapter() {
        binding.hourlyWeatherForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showPlace(place: String) {
        binding.place.text = place
    }

    private fun showCurrentWeatherForecast(data: CurrentWeatherForecastEntity) {
        with(binding) {
            temperatureNow.text = data.temperature
            feelsLikeDegrees.text = data.feelTemperature
            weatherStatus.text = data.weatherDescription
            wind.text = data.windSpeed
            direction.text = data.windDirection
            humidity.text = data.humidity
            pressure.text = data.pressure
            icWeather.setImageResource(WeatherIconCode.valueOf(data.weatherCode).resId)
        }
    }

    private fun showHourlyWeatherForecast(data: List<HourlyWeatherForecastEntity>) {
        binding.hourlyWeatherForecastRecyclerView.adapter = HourlyWeatherForecastAdapter(data)
    }
}