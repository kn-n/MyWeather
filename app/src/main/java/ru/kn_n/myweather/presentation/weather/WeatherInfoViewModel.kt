package ru.kn_n.myweather.presentation.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.kn_n.myweather.domain.entities.ForecastEntity
import ru.kn_n.myweather.domain.entities.PlaceEntity
import ru.kn_n.myweather.domain.interactors.PlacesInteractor
import ru.kn_n.myweather.domain.interactors.WeatherInfoInteractor
import ru.kn_n.myweather.presentation.base.BaseViewModel
import ru.kn_n.myweather.presentation.model.LocationCache
import ru.kn_n.myweather.presentation.navigation.Screens
import ru.kn_n.myweather.utils.Resource
import ru.kn_n.myweather.utils.makePlaceName
import javax.inject.Inject

class WeatherInfoViewModel @Inject constructor(
    private val router: Router,
    private val weatherInfoInteractor: WeatherInfoInteractor,
    private val placesInteractor: PlacesInteractor,
    private val cache: LocationCache
) : BaseViewModel() {

    private val _resultWeather = MutableLiveData<Resource<ForecastEntity>>()
    val resultWeather: LiveData<Resource<ForecastEntity>> = _resultWeather

    private val _place = MutableLiveData<String>()
    val place: LiveData<String> = _place

    fun getWeather() {
        requestWithLiveData(_resultWeather) {
            weatherInfoInteractor.getWeather(cache.placeLat, cache.placeLon)
        }
    }

    fun initCache(){
        if (cache.placeName.isEmpty()){
            cache.placeName = makePlaceName(placesInteractor.getPlace(cache.placeLat, cache.placeLon))
        }
        _place.postValue(cache.placeName)
    }

    fun routToChooseFragment() {
        router.navigateTo(Screens.ChoosePlace())
    }

}