package ru.kn_n.myweather.presentation.choosePlace

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.kn_n.myweather.databinding.FragmentChoosePlaceBinding
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.presentation.ViewModelFactory
import ru.kn_n.myweather.utils.Status
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject

class ChoosePlaceFragment : Fragment() {

    @Inject
    lateinit var viewModel: ChoosePlaceViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentChoosePlaceBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChoosePlaceBinding.inflate(inflater, container, false)

        with(binding){
            searchButton.setOnClickListener {
                if (searchField.text.isNotEmpty()){
                    getFoundPlaces(searchField.text.toString())
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
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
                        showFoundPlaces(data)
                        Log.d("SF", data.toString())
                    }
                    Status.ERROR -> {
                        Log.d("SF", it.message.toString())
                    }
                    Status.LOADING -> {
                        Log.d("SF", "loading")
                    }
                }
            }
        }
    }

    private fun showFoundPlaces(result: MutableList<Address>){
        binding.foundPlacesRecyclerView.adapter = FoundPlacesAdapter(result) { place -> onListItemClick(place) }
    }

    private fun setupAdapter() {
        binding.foundPlacesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun onListItemClick(place:Address) {
        viewModel.goToWeather(place.latitude.toString(), place.longitude.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}