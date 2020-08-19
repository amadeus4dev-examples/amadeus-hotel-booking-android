package com.amadeus.android.demo.fragments.rates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.ItemOfferBinding
import com.amadeus.android.domain.resources.HotelOffer

class RatesAdapter(
    val viewModel: RatesViewModel
) : ListAdapter<HotelOffer.Offer, RatesAdapter.OfferViewHolder>(OfferDiffCallback()) {

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        return OfferViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_offer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemOfferBinding.bind(itemView)

        fun bind(offer: HotelOffer.Offer) {
            val roomDesc = StringBuilder().append(offer.room?.typeEstimated?.category ?: "")
                .append(" - ")
                .append(offer.room?.typeEstimated?.bedType ?: "")
            binding.roomType.text = roomDesc.toString()
            binding.description.text = offer.room?.description?.text ?: "No description"
            val priceText = StringBuilder().append(offer.price?.total ?: "0")
                .append(" ")
                .append(offer.price?.currency)
            offer.policies?.cancellation?.description?.text?.let {
                priceText.append("\n $it")
            }
            offer.boardType?.let {
                priceText.append("\n $it")
            }
            binding.price.text = priceText.toString()
            binding.root.setOnClickListener { view ->
                viewModel.onHotelOfferCLick(view, offer)
            }
        }
    }

    class OfferDiffCallback : DiffUtil.ItemCallback<HotelOffer.Offer>() {
        override fun areItemsTheSame(
            oldItem: HotelOffer.Offer,
            newItem: HotelOffer.Offer
        ): Boolean {
            return oldItem.id != newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HotelOffer.Offer,
            newItem: HotelOffer.Offer
        ): Boolean {
            return oldItem != newItem
        }

    }
}