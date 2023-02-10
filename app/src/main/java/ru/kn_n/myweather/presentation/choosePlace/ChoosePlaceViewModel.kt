package ru.kn_n.myweather.presentation.choosePlace

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ru.kn_n.myweather.domain.entities.GeocodingEntity
import ru.kn_n.myweather.domain.entities.PlaceEntity
import ru.kn_n.myweather.domain.interactors.PlacesInteractor
import ru.kn_n.myweather.presentation.base.BaseViewModel
import ru.kn_n.myweather.presentation.model.LocationCache
import ru.kn_n.myweather.presentation.navigation.Screens
import ru.kn_n.myweather.utils.Constants
import ru.kn_n.myweather.utils.EMPTY
import ru.kn_n.myweather.utils.Resource
import ru.kn_n.myweather.utils.makePlaceName
import javax.inject.Inject

class ChoosePlaceViewModel @Inject constructor(
    private val placesInteractor: PlacesInteractor,
    private val router: Router,
    private val cache: LocationCache
) : BaseViewModel() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray, context: Context) {
        if (requestCode == Constants.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateWithLocationToWeatherInfoFragment(context)
            }
        }
    }

    private val _resultSearch = MutableLiveData<Resource<GeocodingEntity>>()
    val resultSearch: LiveData<Resource<GeocodingEntity>> = _resultSearch

    fun search(name: String){
        requestWithLiveData(_resultSearch){
            placesInteractor.getPlaces(name)
        }
    }

    fun navigateWithPlaceToWeather(place: PlaceEntity) {
        cache.placeLat = place.latitude
        cache.placeLon = place.longitude
        cache.placeName = makePlaceName(place)
        router.navigateTo(Screens.WeatherInfo())
    }

    @SuppressLint("MissingPermission")
    fun navigateWithLocationToWeatherInfoFragment(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            cache.placeLat = location.latitude.toString()
            cache.placeLon = location.longitude.toString()
            cache.placeName = String.EMPTY
            router.navigateTo(Screens.WeatherInfo())
        }
    }
}