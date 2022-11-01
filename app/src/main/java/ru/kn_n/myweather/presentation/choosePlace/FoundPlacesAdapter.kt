package ru.kn_n.myweather.presentation.choosePlace

import android.annotation.SuppressLint
import android.location.Address
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kn_n.myweather.R
import ru.kn_n.myweather.databinding.ItemPlaceBinding
import ru.kn_n.myweather.entities.PlaceEntity

class FoundPlacesAdapter (
    private val data: List<PlaceEntity>,
    private val onItemClick: (place: PlaceEntity) -> Unit,):
    RecyclerView.Adapter<FoundPlacesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPlaceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.city.text = item.name
        holder.binding.country.text = "${item.country}, ${item.admin}"
        holder.binding.place.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = data.size
}