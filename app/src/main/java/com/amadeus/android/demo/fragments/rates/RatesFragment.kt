package com.amadeus.android.demo.fragments.rates

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.FragmentRatesBinding

class RatesFragment : Fragment(R.layout.fragment_rates) {

    private lateinit var binding: FragmentRatesBinding
    private val viewModel by viewModels<RatesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRatesBinding.bind(view)
        initView()
        subscribe()
    }

    private fun initView() {

    }

    private fun subscribe() {

    }

}