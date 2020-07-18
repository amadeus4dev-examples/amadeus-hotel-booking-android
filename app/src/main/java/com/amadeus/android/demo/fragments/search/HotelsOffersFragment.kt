package com.amadeus.android.demo.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.FragmentHotelsOffersBinding
import com.amadeus.android.demo.mainActivity
import com.amadeus.android.demo.utils.gone
import com.amadeus.android.demo.utils.visible
import com.amadeus.android.demo.utils.visibleOrGone
import com.amadeus.android.domain.resources.Location
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId


class HotelsOffersFragment : Fragment(R.layout.fragment_hotels_offers) {

    private lateinit var binding: FragmentHotelsOffersBinding
    private val viewModel by viewModels<HotelsOffersViewModel>()
    private lateinit var adapter: HotelsOffersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHotelsOffersBinding.bind(view)
        mainActivity()?.setSupportActionBar(binding.toolbar)
        initView()
        subscribe()
    }

    private fun initView() {
        if (!this::adapter.isInitialized) {
            adapter = HotelsOffersAdapter(viewModel)
        }
        binding.locationPicker.setOnClickListener {
            binding.locationPicker.setOnClickListener(null)
            it.findNavController().navigate(R.id.locationFragment)
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
        binding.datePicker.setOnClickListener {
            val constraints = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()
            MaterialDatePicker.Builder.dateRangePicker()
                .setCalendarConstraints(constraints)
                .build()
                .apply {
                    addOnPositiveButtonClickListener { selection ->
                        findNavController()
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.apply {
                                set(
                                    PAIR_DATE_RESULT_KEY,
                                    Pair(
                                        Instant.ofEpochMilli(selection.first ?: 0)
                                            .atZone(ZoneId.systemDefault()).toLocalDate(),
                                        Instant.ofEpochMilli(selection.second ?: 0)
                                            .atZone(ZoneId.systemDefault()).toLocalDate()
                                    )
                                )
                            }
                    }
                }.show(childFragmentManager, "datePicker")
        }
        binding.search.setOnClickListener {
            findNavController()
                .currentBackStackEntry
                ?.savedStateHandle
                ?.apply {
                    it.isEnabled = false
                    binding.datePicker.isEnabled = false
                    binding.locationPicker.isEnabled = false
                    val location = getLiveData<Location>(LOCATION_RESULT_KEY).value
                    val dates = getLiveData<Pair<LocalDate, LocalDate>>(PAIR_DATE_RESULT_KEY).value
                    if (location != null && dates != null) {
                        viewModel.searchByDestination(location.iataCode ?: "", dates.first, dates.second)
                    }
                }
        }
    }

    private fun subscribe() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.noDataText.gone(true)
            }
            binding.progressBar.visibleOrGone(isLoading, true)
            binding.search.isEnabled = !isLoading
            binding.datePicker.isEnabled = !isLoading
            binding.locationPicker.isEnabled = !isLoading
        }
        viewModel.error.observe(viewLifecycleOwner) { message ->
            binding.noDataText.text = message
            binding.recyclerView.gone(true)
            binding.progressBar.gone(true)
            binding.noDataText.visible(true)
        }
        viewModel.hotelOffers.observe(viewLifecycleOwner) { hotelOffers ->
            adapter.submitList(hotelOffers)
            binding.recyclerView.visible(true)
            binding.noDataText.gone(true)
        }
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.apply {
                getLiveData<Location>(LOCATION_RESULT_KEY).observe(viewLifecycleOwner) { location ->
                    binding.locationPicker.text = "${location.name}"
                }
                getLiveData<Pair<LocalDate, LocalDate>>(PAIR_DATE_RESULT_KEY).observe(
                    viewLifecycleOwner
                ) { pair ->
                    binding.datePicker.text = "${pair.first} - ${pair.second}"
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        binding.locationPicker.setOnClickListener(null)
        binding.datePicker.setOnClickListener(null)
    }

    companion object {
        const val LOCATION_RESULT_KEY = "location_result"
        const val PAIR_DATE_RESULT_KEY = "date_result"
    }

}
