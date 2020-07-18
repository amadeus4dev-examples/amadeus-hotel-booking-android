package com.amadeus.android.demo.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadeus.android.ApiResult.Error
import com.amadeus.android.ApiResult.Success
import com.amadeus.android.demo.SampleApplication
import com.amadeus.android.demo.utils.SingleLiveEvent
import com.amadeus.android.domain.resources.HotelOffer
import com.amadeus.android.succeeded
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class HotelsOffersViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val error = SingleLiveEvent<String>()

    private var latestResult: Success<List<HotelOffer>>? = null

    private val _hotelOffers = MutableLiveData<List<HotelOffer>>()
    val hotelOffers: LiveData<List<HotelOffer>>
        get() = _hotelOffers

    fun searchByDestination(
        destination: String,
        checkInDate: LocalDate?,
        checkOutDate: LocalDate?
    ) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = SampleApplication.amadeus.shopping.hotelOffers.get(
                cityCode = destination,
                checkInDate = checkInDate.toString(),
                checkOutDate = checkOutDate.toString()
            )) {
                is Success -> {
                    if (result.succeeded) {
                        latestResult = result
                        _hotelOffers.value = result.data
                    } else {
                        //call return without data
                        error.value = "No result for your research"
                    }
                }
                is Error -> error.value = "Error when retrieving data."
            }
            _loading.value = false
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            latestResult?.let {
                when (val next = SampleApplication.amadeus.next(it)) {
                    is Success -> {
                        if (next.succeeded) {
                            latestResult = next
                            val newList = ArrayList(_hotelOffers.value.orEmpty())
                            newList.addAll(next.data)
                            _hotelOffers.value = newList
                        } else {
                            //call return without data
                            error.value = "No result for your research"
                        }
                    }
                }
            }
        }
    }
}
