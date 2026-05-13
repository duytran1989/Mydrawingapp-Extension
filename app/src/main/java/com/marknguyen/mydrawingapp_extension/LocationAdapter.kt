package com.marknguyen.mydrawingapp_extension

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

class LocationAdapter(
    private val locations: MutableList<Location>,
    private val onItemClick: (Int, Location) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivHero)
        val textName: TextView = view.findViewById(R.id.tvLocationName)
        val textCity: TextView = view.findViewById(R.id.tvCountry)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarItem)
        val chipVisited: Chip = view.findViewById(R.id.chipVisited)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        with(holder) {
            imageView.setImageResource(location.imageResId)
            imageView.contentDescription = location.name
            textName.text = location.name
            textCity.text = location.city
            ratingBar.rating = location.rating

            val visited = location.isVisited
            chipVisited.text = itemView.context.getString(
                if (visited) R.string.label_visited_yes else R.string.label_visited_no
            )
            chipVisited.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context,
                    if (visited) R.color.chip_visited else R.color.chip_not_visited)
            )
            chipVisited.setTextColor(
                ContextCompat.getColor(itemView.context,
                    if (visited) R.color.white else R.color.textSecondary)
            )

            itemView.setOnClickListener {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onItemClick(pos, locations[pos])
            }
        }
    }

    override fun getItemCount() = locations.size
}
