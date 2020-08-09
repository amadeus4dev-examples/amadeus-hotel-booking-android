package com.amadeus.android.demo.fragments.price

import androidx.lifecycle.*
import com.amadeus.android.ApiResult.Success
import com.amadeus.android.demo.SampleApplication
import com.amadeus.android.demo.utils.SingleLiveEvent
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

    val bookingResult = SingleLiveEvent<String>()

    init {
        fetchPrice()
    }

    private fun fetchPrice() {
        viewModelScope.launch {
            _loading.value = true
            val amadeus = SampleApplication.amadeus
            when (val result =
                amadeus.shopping.hotelOffer(offerId).get()) {
                is Success -> {
                    _hotelOffer.value = result.data
                }
                else -> {
                }
            }
            _loading.value = false
        }
    }

    fun postHotelBooking() {
        viewModelScope.launch {
            _loading.value = true
            val amadeus = SampleApplication.amadeus
            // Fake user
            val name = mapOf(
                "firstName" to "John",
                "lastName" to "Doe"
            )
            val contact = mapOf(
                "phone" to "+33679278416",
                "email" to "john.doe@email.com"
            )
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
            val hotelBookingQuery = mapOf<String, Any>(
                "offerId" to offerId,
                "guests" to arrayOf(
                    mapOf(
                        "name" to name,
                        "contact" to contact,
                        "payments" to payments
                    )
                )
            )
            when (amadeus.booking.hotelBooking.post(mapOf("data" to hotelBookingQuery))) {
                is Success -> bookingResult.value = "Booking accepted."
                else -> bookingResult.value = "Error with your booking."
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