package com.amadeus.android.demo.fragments.price

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.FragmentPriceBinding
import com.amadeus.android.demo.mainActivity
import com.amadeus.android.demo.utils.visible
import com.amadeus.android.demo.utils.visibleOrGone
import com.google.android.material.snackbar.Snackbar

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
        binding.book.setOnClickListener {
            val errorString = getString(R.string.error)
            var haveError = false
            binding.firstNameInput.apply {
                error = if (editText?.text.isNullOrBlank()) {
                    haveError = true
                    errorString
                } else {
                    null
                }
            }
            binding.lastNameInput.apply {
                error = if (editText?.text.isNullOrBlank()) {
                    haveError = true
                    errorString
                } else {
                    null
                }
            }
            binding.phoneInput.apply {
                error = if (editText?.text.isNullOrBlank()) {
                    haveError = true
                    errorString
                } else {
                    null
                }
            }
            binding.emailInput.apply {
                error = if (editText?.text.isNullOrBlank()) {
                    haveError = true
                    errorString
                } else {
                    null
                }
            }
            if (!haveError) {
                viewModel.postHotelBooking(
                    binding.firstNameInput.editText!!.text.toString(),
                    binding.lastNameInput.editText!!.text.toString(),
                    binding.phoneInput.editText!!.text.toString(),
                    binding.emailInput.editText!!.text.toString()
                )
            }
        }
        val builder = StringBuilder("PAYMENT\n\n")
        viewModel.payments.firstOrNull()?.entries?.forEach {
            builder.append(it.key)
                .append(": ")
                .append(it.value)
                .append("\n")
        }
        binding.payment.text = builder.toString()
    }

    private fun subscribe() {
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.visibleOrGone(it) }
        viewModel.hotelOffer.observe(viewLifecycleOwner) { hotelOffer ->
            binding.book.visible()
            binding.toolbar.title = hotelOffer.hotel?.name ?: "Hotel"
        }
        viewModel.bookingResult.observe(viewLifecycleOwner) {
            it?.let {
                binding.book.isEnabled = false
                binding.payment.append("\n\nBooking id: ${it.id}")
                Snackbar.make(binding.root, "Booking completed.", Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.book.setOnClickListener(null)
    }

}