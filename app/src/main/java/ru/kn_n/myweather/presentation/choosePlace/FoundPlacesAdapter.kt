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

class FoundPlacesAdapter (
    private val data:  MutableList<Address>,
    private val onItemClick: (place: Address) -> Unit,):
    RecyclerView.Adapter<FoundPlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.city.text = item.featureName
        holder.country.text = "${item.countryName}, ${item.adminArea}"
        holder.place.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val city: TextView = itemView.findViewById(R.id.city)
        val country: TextView = itemView.findViewById(R.id.country)
        val place: LinearLayout = itemView.findViewById(R.id.place)
    }
}