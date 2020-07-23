package com.amadeus.android.demo.fragments.rates

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.FragmentRatesBinding
import com.amadeus.android.demo.mainActivity
import com.amadeus.android.demo.utils.visibleOrGone

class RatesFragment : Fragment(R.layout.fragment_rates) {

    private val args by navArgs<RatesFragmentArgs>()

    private lateinit var binding: FragmentRatesBinding

    private lateinit var adapter: RatesAdapter

    private val viewModel: RatesViewModel by viewModels {
        RatesViewModel.Factory(
            args.hotelId,
            args.checkInDate,
            args.checkOutDate
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRatesBinding.bind(view)
        initView()
        subscribe()
    }

    private fun initView() {
        mainActivity()?.setSupportActionBar(binding.toolbar)
        mainActivity()?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (!this::adapter.isInitialized) {
            adapter = RatesAdapter(viewModel)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            ).apply {
                setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.divider_default
                    )!!
                )
            })
    }

    private fun subscribe() {
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.visibleOrGone(it) }
        viewModel.hotelOffer.observe(viewLifecycleOwner) { hotelOffer ->
            binding.toolbar.title = hotelOffer.hotel?.name ?: "Hotel"
            adapter.submitList(hotelOffer.offers)
        }
    }

}