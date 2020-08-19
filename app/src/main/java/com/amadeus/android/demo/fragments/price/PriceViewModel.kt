package com.amadeus.android.demo.fragments.price

import androidx.lifecycle.*
import com.amadeus.android.ApiResult.Success
import com.amadeus.android.demo.SampleApplication
import com.amadeus.android.demo.utils.SingleLiveEvent
import com.amadeus.android.domain.resources.HotelBooking
import com.amadeus.android.domain.resources.HotelOffer
import kotlinx.coroutines.launch

class PriceViewModel(
    private val offerId: String
) : ViewModel() {

    private val _hotelOffer = MutableLiveData<HotelOffer>()
    val hotelOffer: LiveData<HotelOffer>
        get() = _hotelOffer

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val error = SingleLiveEvent<String>()

    val bookingResult = SingleLiveEvent<HotelBooking?>()

    val payments = arrayOf(
        mapOf(
            "method" to "creditCard",
            "card" to mapOf(
                "vendorCode" to "VI",
                "cardNumber" to "4111111111111111",
                "expiryDate" to "2023-01"
            )
        )
    )

    init {
        fetchPrice()
    }

    private fun fetchPrice() {
        viewModelScope.launch {
            _loading.value = true
            val amadeus = SampleApplication.amadeus
            when (val result = amadeus.shopping.hotelOffer(offerId).get()) {
                is Success -> {
                    _hotelOffer.value = result.data
                }
                else -> {
                }
            }
            _loading.value = false
        }
    }

    fun postHotelBooking(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            val amadeus = SampleApplication.amadeus
            val name = mapOf(
                "firstName" to firstName,
                "lastName" to lastName
            )
            val contact = mapOf(
                "phone" to phone,
                "email" to email
            )
            val hotelBookingQuery = mapOf<String, Any>(
                "offerId" to offerId,
                "guests" to arrayOf(
                    mapOf(
                        "name" to name,
                        "contact" to contact
                    )
                ),
                "payments" to payments
            )
            when (val result = amadeus.booking.hotelBooking.post(mapOf("data" to hotelBookingQuery))) {
                is Success -> bookingResult.value = result.data.firstOrNull()
                else -> error.value = "Error with your booking."
            }
            _loading.value = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val offerId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PriceViewModel(offerId) as T
        }
    }

}