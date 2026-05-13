package com.marknguyen.mydrawingapp_extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(
    private val locations: List<Location>,
    private val onItemClick: (Int, Location) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivHero)
        val textName: TextView = view.findViewById(R.id.tvLocationName)
        val textCity: TextView = view.findViewById(R.id.tvCountry)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        with(holder) {
            imageView.setImageResource(location.imageResId)
            textName.text = location.name
            textCity.text = location.city
            ratingBar.rating = location.rating
            imageView.contentDescription = location.name
            itemView.setOnClickListener { onItemClick(position, location) }
        }
    }

    override fun getItemCount() = locations.size
}
