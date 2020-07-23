package com.amadeus.android.demo.fragments.rates

import androidx.lifecycle.*
import com.amadeus.android.ApiResult.Success
import com.amadeus.android.demo.SampleApplication
import com.amadeus.android.demo.utils.SingleLiveEvent
import com.amadeus.android.domain.resources.HotelOffer
import kotlinx.coroutines.launch

class RatesViewModel(
    private val hotelId: String,
    private val checkInDate: String,
    private val checkOutDate: String
) : ViewModel() {

    private val _hotelOffer = MutableLiveData<HotelOffer>()
    val hotelOffer: LiveData<HotelOffer>
        get() = _hotelOffer

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val error = SingleLiveEvent<String>()

    init {
        fetchRates()
    }

    private fun fetchRates() {
        viewModelScope.launch {
            _loading.value = true
            val amadeus = SampleApplication.amadeus
            when (val result =
                amadeus.shopping.hotelOffersByHotel.get(hotelId, checkInDate, checkOutDate)) {
                is Success -> {
                    _hotelOffer.value = result.data
                }
                else -> {
                }
            }
            _loading.value = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val hotelId: String,
        private val checkInDate: String,
        private val checkOutDate: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RatesViewModel(hotelId, checkInDate, checkOutDate) as T
        }
    }

}