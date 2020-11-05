package com.amadeus.android.demo.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.ItemHotelOffersBinding
import com.amadeus.android.domain.resources.HotelOffer

class HotelsOffersAdapter(
    private val viewModel: HotelsOffersViewModel
) : PagingDataAdapter<HotelOffer, HotelsOffersAdapter.HotelOffersViewHolder>(HOTEL_OFFER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelOffersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hotel_offers, parent, false)
        return HotelOffersViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelOffersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HotelOffersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemHotelOffersBinding.bind(itemView)

        fun bind(item: HotelOffer?) {
            item?.let { hotelOffer ->
                itemView.setOnClickListener { viewModel.onHotelOfferClick(it, hotelOffer) }
                binding.hotelTitle.text = hotelOffer.hotel?.name
                hotelOffer.hotel?.address?.let { address ->
                    val builder = StringBuilder()
                    address.lines?.forEach { builder.append("$it ") }
                    address.postalCode?.let { builder.append(", $it") }
                    address.cityName?.let { builder.append(" $it") }
                    binding.hotelAddress.text = builder.toString()
                }
                val builder = StringBuilder(hotelOffer.offers?.firstOrNull()?.price?.total ?: "")
                    .append(" ")
                    .append(hotelOffer.offers?.firstOrNull()?.price?.currency ?: "")
                binding.offersCount.text = builder.toString()
            }
        }
    }

    companion object {
        private val HOTEL_OFFER_COMPARATOR = object : DiffUtil.ItemCallback<HotelOffer>() {
            override fun areItemsTheSame(oldItem: HotelOffer, newItem: HotelOffer): Boolean =
                oldItem.hotel?.hotelId == newItem.hotel?.hotelId

            override fun areContentsTheSame(oldItem: HotelOffer, newItem: HotelOffer): Boolean =
                oldItem == newItem
        }
    }
}