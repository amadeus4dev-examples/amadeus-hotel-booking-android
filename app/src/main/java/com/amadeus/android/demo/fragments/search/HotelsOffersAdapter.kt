package com.amadeus.android.demo.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.ItemHotelOffersBinding
import com.amadeus.android.demo.databinding.ItemLoadMoreBinding
import com.amadeus.android.demo.utils.BindableViewHolder
import com.amadeus.android.demo.utils.DisplayableElement
import com.amadeus.android.domain.resources.HotelOffer

class HotelsOffersAdapter(
    private val viewModel: HotelsOffersViewModel
) : ListAdapter<DisplayableElement<HotelOffer>, BindableViewHolder<HotelOffer?>>(
    HotelOffersDiffCallback()
) {

    override fun getItemId(position: Int): Long {
        return getItem(position).element?.hotel?.hotelId.hashCode().toLong()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindableViewHolder<HotelOffer?> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_hotel_offers -> HotelOffersViewHolder(view)
            else -> LoadMoreViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BindableViewHolder<HotelOffer?>, position: Int) {
        holder.bind(getItem(position).element)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            DisplayableElement.Type.ELEMENT -> R.layout.item_hotel_offers
            else -> R.layout.item_load_more
        }
    }

    inner class HotelOffersViewHolder(itemView: View) : BindableViewHolder<HotelOffer?>(itemView) {

        private val binding = ItemHotelOffersBinding.bind(itemView)

        override fun bind(element: HotelOffer?) {
            element?.let { hotelOffer ->
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

    inner class LoadMoreViewHolder(itemView: View) : BindableViewHolder<HotelOffer?>(itemView) {

        override fun bind(element: HotelOffer?) {
            viewModel.loadMore()
        }
    }

    class HotelOffersDiffCallback : DiffUtil.ItemCallback<DisplayableElement<HotelOffer>>() {
        override fun areItemsTheSame(
            oldItem: DisplayableElement<HotelOffer>,
            newItem: DisplayableElement<HotelOffer>
        ): Boolean {
            return oldItem.element?.hotel?.hotelId == newItem.element?.hotel?.hotelId
        }

        override fun areContentsTheSame(
            oldItem: DisplayableElement<HotelOffer>,
            newItem: DisplayableElement<HotelOffer>
        ): Boolean {
            return oldItem == newItem
        }
    }
}