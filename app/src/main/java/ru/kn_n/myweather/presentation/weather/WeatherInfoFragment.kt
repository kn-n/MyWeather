package ru.kn_n.myweather.presentation.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ru.kn_n.myweather.databinding.FragmentWeatherInfoBinding
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.presentation.ViewModelFactory
import ru.kn_n.myweather.utils.Constants.PERMISSION_ID
import ru.kn_n.myweather.utils.Status
import ru.kn_n.myweather.utils.checkLocationPermissions
import ru.kn_n.myweather.utils.requestPermission
import toothpick.Toothpick.openScope
import javax.inject.Inject

class WeatherInfoFragment : Fragment() {

    @Inject
    lateinit var viewModel: WeatherInfoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentWeatherInfoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeatherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()

        binding.placeContainer.setOnClickListener {
            viewModel.routToChooseFragment()
        }

        binding.update.setOnClickListener {
            getWeatherFromLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        getWeatherFromLocation()
    }

    private fun setupViewModel() {
        viewModelFactory = openScope(Scopes.APP_SCOPE).getInstance(ViewModelFactory::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherInfoViewModel::class.java]
    }

    private fun getWeatherFromLocation() {
        viewModel.getLocation(requireContext()).observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data!!
                        getWeather(data.result.latitude.toString(), data.result.longitude.toString())
                    }
                    Status.ERROR -> {
                        Log.d("GL", it.message.toString())
                    }
                    Status.LOADING -> {
                        Log.d("GL", "loading")
                    }
                    Status.EMPTY -> {
                        getWeather("56.897699", "60.626907")
                    }
                }
            }
        }
    }

    private fun getWeather(lat: String, lon: String) {
        viewModel.getWeather(lat, lon).observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data!!
                        showCurrentWeatherForecast(data.currentForecast)
                        showHourlyWeatherForecast(data.hourlyForecast)
                    }
                    Status.ERROR -> {
                        Log.d("WF", it.message.toString())
                    }
                    Status.LOADING -> {
                        Log.d("WF", "loading")
                    }
                }
            }
        }

        viewModel.getPlace(lat, lon).observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data!!
                        showPlace(data[0])
                    }
                    Status.ERROR -> {
                        Log.d("GP", it.message.toString())
                    }
                    Status.LOADING -> {
                        Log.d("GP", "loading")
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.hourlyWeatherForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    @SuppressLint("SetTextI18n")
    private fun showPlace(address: Address){
        binding.place.text = address.getAddressLine(0)
    }

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