package ru.kn_n.myweather.presentation.weather

import android.annotation.SuppressLint
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.kn_n.myweather.databinding.FragmentWeatherInfoBinding
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.presentation.ViewModelFactory
import ru.kn_n.myweather.utils.Status
import toothpick.Toothpick.openScope
import javax.inject.Inject

const val PLACE_LAT = "place_lat"
const val PLACE_LON = "place_lon"

class WeatherInfoFragment : Fragment() {

    @Inject
    lateinit var viewModel: WeatherInfoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var placeLat = ""
    private var placeLon = ""

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

        arguments?.takeIf { it.containsKey(PLACE_LAT) && it.containsKey(PLACE_LON) }?.apply {
            placeLat = getString(PLACE_LAT)!!
            placeLon = getString(PLACE_LON)!!
        }

        binding.placeContainer.setOnClickListener {
            viewModel.routToChooseFragment()
        }

        binding.update.setOnClickListener {
            getWeatherFromLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        if (placeLat.isEmpty() && placeLon.isEmpty()){
            getWeatherFromLocation()
        } else {
            getWeather(placeLat, placeLon)
            Log.d("PC", "$placeLat | $placeLon")
        }
    }

    private fun setupViewModel() {
        viewModelFactory = openScope(Scopes.APP_SCOPE).getInstance(ViewModelFactory::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherInfoViewModel::class.java]
    }

    @SuppressLint("MissingPermission")
    private fun getWeatherFromLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            Log.d("GL", location.toString())
            getWeather(location.longitude.toString(), location.longitude.toString())
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
    private fun showPlace(address: Address) {
        binding.place.text = "${address.countryName}, ${address.adminArea}, ${address.subAdminArea}"
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
        fun instance(lat: String, lon: String): WeatherInfoFragment {
            val fragment = WeatherInfoFragment()
            fragment.arguments = Bundle().apply { putString(PLACE_LAT, lat)
                                                putString(PLACE_LON, lon)}
            return fragment
        }
    }
}