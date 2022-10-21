package ru.kn_n.myweather.presentation.choosePlace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.kn_n.myweather.data.repositories.PlacesRepository
import ru.kn_n.myweather.utils.Resource
import javax.inject.Inject

class ChoosePlaceViewModel @Inject constructor(private val placesRepository: PlacesRepository): ViewModel() {

    fun search(name: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = placesRepository.getPlaces(name)))
        } catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}