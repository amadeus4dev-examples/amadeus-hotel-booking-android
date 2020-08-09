package com.amadeus.android.demo.fragments.price

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.FragmentPriceBinding
import com.amadeus.android.demo.databinding.FragmentRatesBinding
import com.amadeus.android.demo.mainActivity
import com.amadeus.android.demo.utils.visible
import com.amadeus.android.demo.utils.visibleOrGone
import com.amadeus.android.tools.TypesAdapterFactory
import com.amadeus.android.tools.XNullableAdapterFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.withContext

class PriceFragment : Fragment(R.layout.fragment_price) {

    private val args by navArgs<PriceFragmentArgs>()

    private lateinit var binding: FragmentPriceBinding

    private val viewModel: PriceViewModel by viewModels {
        PriceViewModel.Factory(
            args.offerId
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPriceBinding.bind(view)
        initView()
        subscribe()
    }

    private fun initView() {
        mainActivity()?.setSupportActionBar(binding.toolbar)
        mainActivity()?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.book.setOnClickListener { viewModel.postHotelBooking() }
    }

    private fun subscribe() {
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.visibleOrGone(it) }
        viewModel.hotelOffer.observe(viewLifecycleOwner) { hotelOffer ->
            binding.book.visible()
            binding.toolbar.title = hotelOffer.hotel?.name ?: "Hotel"
            binding.text.text = hotelOffer.toString()
        }
        viewModel.bookingResult.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.book.setOnClickListener(null)
    }

}