package com.amadeus.android.demo.fragments.location

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.amadeus.android.ApiResult
import com.amadeus.android.demo.SampleApplication
import com.amadeus.android.demo.fragments.search.HotelsOffersFragment.Companion.LOCATION_RESULT_KEY
import com.amadeus.android.demo.utils.SingleLiveEvent
import com.amadeus.android.domain.resources.Location
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val error = SingleLiveEvent<String>()

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>>
        get() = _locations

    fun searchLocations(location: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result =
                SampleApplication.amadeus.referenceData.locations.get(listOf("CITY"), location)) {
                is ApiResult.Success -> _locations.value = result.data
                else -> error.value = "Something wrong happened with your request."
            }
            _loading.value = false
        }
    }

    fun onLocationSelected(view: View, location: Location) {
        view.findNavController().apply {
            previousBackStackEntry?.savedStateHandle?.set(LOCATION_RESULT_KEY, location)
            popBackStack()
        }
    }
}