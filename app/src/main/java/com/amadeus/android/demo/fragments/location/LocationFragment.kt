package com.amadeus.android.demo.fragments.location

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.FragmentLocationBinding
import com.amadeus.android.demo.mainActivity
import com.amadeus.android.demo.utils.gone
import com.amadeus.android.demo.utils.hideKeyboard
import com.amadeus.android.demo.utils.visible
import com.amadeus.android.demo.utils.visibleOrGone

class LocationFragment : Fragment(
    R.layout.fragment_location
) {

    private val viewModel by viewModels<LocationViewModel>()

    private lateinit var binding: FragmentLocationBinding

    private lateinit var adapter: LocationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationBinding.bind(view)
        initView()
        subscribeView()
    }

    private fun initView() {
        if (!this::adapter.isInitialized) {
            adapter = LocationsAdapter(viewModel)
        }
        mainActivity()?.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.destinationInput.editText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchLocations(
                    v.text.toString()
                )
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
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

    private fun subscribeView() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.noDataText.gone(true)
            }
            binding.progressBar.visibleOrGone(loading)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.noDataText.text = it
            binding.noDataText.visible()
        }
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            adapter.submitList(locations)
            binding.recyclerView.visibleOrGone(locations.isNotEmpty())
            if (locations.isEmpty()) {
                binding.noDataText.text = "No locations for this keyword"
            }
            binding.noDataText.visibleOrGone(locations.isEmpty())
        }
    }

}