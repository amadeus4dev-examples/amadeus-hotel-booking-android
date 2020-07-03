package com.amadeus.android.demo.fragments.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.ItemLocationsBinding
import com.amadeus.android.demo.fragments.location.LocationsAdapter.LocationsViewHolder
import com.amadeus.android.domain.resources.Location

class LocationsAdapter(
    val viewModel: LocationViewModel
) : ListAdapter<Location, LocationsViewHolder>(LocationsDiffCallback()) {

    override fun getItemId(position: Int): Long {
        return getItem(position).name.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        return LocationsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_locations, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LocationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemLocationsBinding.bind(itemView)

        fun bind(location: Location) {
            binding.name.text = location.name ?: "Unknown name"
            binding.root.setOnClickListener { view ->
                viewModel.onLocationSelected(view, location)
            }
        }
    }

    class LocationsDiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.name != newItem.name
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem != newItem
        }

    }
}