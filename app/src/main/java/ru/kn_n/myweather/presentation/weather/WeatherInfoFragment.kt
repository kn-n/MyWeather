package ru.kn_n.myweather.presentation.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetferrytan.skeletonplaceholderview.RectBone
import ru.kn_n.myweather.R
import ru.kn_n.myweather.databinding.FragmentWeatherInfoBinding
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.entities.CurrentWeatherForecastEntity
import ru.kn_n.myweather.entities.HourlyWeatherForecastEntity
import ru.kn_n.myweather.presentation.ViewModelFactory
import ru.kn_n.myweather.utils.Status
import ru.kn_n.myweather.utils.gone
import ru.kn_n.myweather.utils.makePlaceName
import ru.kn_n.myweather.utils.show
import toothpick.Toothpick.openScope
import javax.inject.Inject

class WeatherInfoFragment : Fragment() {

    @Inject
    lateinit var viewModel: WeatherInfoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var placeLat = ""
    private var placeLon = ""
    private var placeName = ""

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
        setUpSkeleton()

        arguments?.takeIf { it.containsKey(PLACE_LAT) && it.containsKey(PLACE_LON) && it.containsKey(PLACE_NAME)}?.apply {
            placeLat = getString(PLACE_LAT)!!
            placeLon = getString(PLACE_LON)!!
            placeName = getString(PLACE_NAME)!!
        }

        if (placeName.isEmpty()){
            getPlaceName()
        } else {
            showPlace(placeName)
        }


        binding.placeContainer.setOnClickListener {
            viewModel.routToChooseFragment()
        }

        binding.update.setOnClickListener {
            getWeather(placeLat, placeLon)
        }
    }

    override fun onResume() {
        super.onResume()
        getWeather(placeLat, placeLon)
        Log.d("PC", "$placeLat | $placeLon")
    }

    private fun setupViewModel() {
        viewModelFactory = openScope(Scopes.APP_SCOPE).getInstance(ViewModelFactory::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherInfoViewModel::class.java]
    }

    private fun getWeather(lat: String, lon: String) {
        viewModel.getWeather(lat, lon).observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data!!
                        stopSkeleton()
                        showCurrentWeatherForecast(data.currentForecast)
                        showHourlyWeatherForecast(data.hourlyForecast)
                    }
                    Status.ERROR -> {
                        stopSkeleton()
                        Log.d("WF", it.message.toString())
                    }
                    Status.LOADING -> {
                        showSkeleton()
                        Log.d("WF", "loading")
                    }
                }
            }
        }
    }

    private fun getPlaceName(){
        viewModel.getPlace(placeLat, placeLon).observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data!!
                        showPlace(makePlaceName(data))
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

    private fun setUpSkeleton(){
        binding.skeleton.skeletonPlaceholderView.skinView(R.layout.fragment_weather_info,
            RectBone(R.id.weather_card),
            RectBone(R.id.weather_params),
            RectBone(R.id.place_container)
        )
        binding.skeleton.shimmer.startShimmer()
    }

    private fun showSkeleton(){
        binding.skeleton.skeletonPlaceholderView.show()
    }

    private fun stopSkeleton(){
        binding.skeleton.skeletonPlaceholderView.gone()
    }

    private fun setupAdapter() {
        binding.hourlyWeatherForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    @SuppressLint("SetTextI18n")
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

        const val PLACE_LAT = "place_lat"
        const val PLACE_LON = "place_lon"
        const val PLACE_NAME = "place_name"

        fun instance(lat: String, lon: String, name:String): WeatherInfoFragment {
            val fragment = WeatherInfoFragment()
            fragment.arguments = Bundle().apply {
                putString(PLACE_LAT, lat)
                putString(PLACE_LON, lon)
                putString(PLACE_NAME, name)
            }
            return fragment
        }
    }
}