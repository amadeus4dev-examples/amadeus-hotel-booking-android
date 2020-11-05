package com.amadeus.android.demo.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.FragmentHotelsOffersBinding
import com.amadeus.android.demo.fragments.search.paging.ReposLoadStateAdapter
import com.amadeus.android.demo.mainActivity
import com.amadeus.android.demo.utils.gone
import com.amadeus.android.demo.utils.visible
import com.amadeus.android.domain.resources.Location
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId


class HotelsOffersFragment : Fragment(R.layout.fragment_hotels_offers) {

    private lateinit var binding: FragmentHotelsOffersBinding
    private val viewModel by viewModels<HotelsOffersViewModel>()
    private lateinit var adapter: HotelsOffersAdapter

    private var searchJob: Job? = null

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
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )
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
                    val location = getLiveData<Location>(LOCATION_RESULT_KEY).value
                    val dates = getLiveData<Pair<LocalDate, LocalDate>>(PAIR_DATE_RESULT_KEY).value
                    if (location != null && dates != null) {
                        binding.recyclerView.scrollToPosition(0)
                        search(location, dates)
                    }
                }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    binding.progressBar.gone()
                    binding.recyclerView.scrollToPosition(0)
                }
        }
    }

    private fun search(location: Location, dates: Pair<LocalDate, LocalDate>) {
        binding.progressBar.visible()
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchByDestination(
                location.iataCode ?: "",
                dates.first,
                dates.second
            ).collectLatest { adapter.submitData(it) }
        }
    }

    private fun subscribe() {
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
