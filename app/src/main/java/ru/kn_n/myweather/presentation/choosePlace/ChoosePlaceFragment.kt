package ru.kn_n.myweather.presentation.choosePlace

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kn_n.myweather.databinding.FragmentChoosePlaceBinding
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.entities.PlaceEntity
import ru.kn_n.myweather.presentation.ViewModelFactory
import ru.kn_n.myweather.utils.*
import toothpick.Toothpick
import javax.inject.Inject

class ChoosePlaceFragment : Fragment() {

    @Inject
    lateinit var viewModel: ChoosePlaceViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentChoosePlaceBinding? = null

    private val binding get() = _binding!!

    private var queryTextChangedJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChoosePlaceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()
        setupSearchView()

        hiddenErrorText(binding.error)
    }

    override fun onResume() {
        super.onResume()

        with(binding) {

            findMe.setOnClickListener {
                if (haveLocationPermissions(requireContext())) {
                    viewModel.navigateWithLocationToWeatherInfoFragment(requireContext())
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        Constants.PERMISSION_ID
                    )
                }
            }

            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(text: String?): Boolean {
                        if (!text.isNullOrEmpty()) {
                            getFoundPlaces(text)
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!newText.isNullOrEmpty()) {
                            queryTextChangedJob?.cancel()
                            queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                                Log.d("SF", "$newText change")
                                delay(500)
                                getFoundPlaces(newText)
                            }
                        }
                        return false
                    }
                }
            )
        }
    }

    private fun setupSearchView() {
        with(binding.searchView) {
            findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon).setColorFilter(Color.WHITE)
            findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).setColorFilter(Color.WHITE)
            findViewById<TextView>(androidx.appcompat.R.id.search_src_text).setTextColor(Color.WHITE)
            findViewById<TextView>(androidx.appcompat.R.id.search_src_text).textSize = 18f
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, grantResults, requireContext())
    }

    private fun setupViewModel() {
        viewModelFactory = Toothpick.openScope(Scopes.APP_SCOPE).getInstance(ViewModelFactory::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[ChoosePlaceViewModel::class.java]
    }

    private fun getFoundPlaces(query: String) {
        viewModel.search(query).observe(viewLifecycleOwner) {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val data = resource.data!!
                        doOnSuccess(data.listOfPlaces)
                    }
                    Status.ERROR -> {
//                        doOnError()
                        Log.d("SF", it.message.toString())
                    }
                    Status.LOADING -> {
                        doOnLoading()
                    }
                }
            }
        }
    }

    private fun doOnSuccess(places: List<PlaceEntity>) {
        binding.foundPlacesRecyclerView.show()
        stopCircularLoading(binding.loading)
        showFoundPlaces(places)
        Log.d("SF", places.toString())
    }

    private fun doOnError() {
        stopCircularLoading(binding.loading)
        showErrorText(binding.error)
        binding.foundPlacesRecyclerView.hide()
    }

    private fun doOnLoading() {
        hiddenErrorText(binding.error)
        startCircularLoading(binding.loading)
        binding.foundPlacesRecyclerView.hide()
        Log.d("SF", "loading")
    }

    private fun showFoundPlaces(result: List<PlaceEntity>) {
        binding.foundPlacesRecyclerView.adapter = FoundPlacesAdapter(result) { place -> onListItemClick(place) }
    }

    private fun setupAdapter() {
        binding.foundPlacesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun onListItemClick(place: PlaceEntity) {
        viewModel.navigateWithPlaceToWeather(place)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}