package com.amadeus.android.demo.fragments.search

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amadeus.android.ApiResult.Success
import com.amadeus.android.demo.R
import com.amadeus.android.demo.fragments.rates.RatesFragmentArgs
import com.amadeus.android.demo.fragments.search.paging.HotelsOffersPagingSource
import com.amadeus.android.demo.utils.SingleLiveEvent
import com.amadeus.android.domain.resources.HotelOffer
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import timber.log.Timber

class HotelsOffersViewModel : ViewModel() {

    private var currentDestination: String? = null
    private var currentSearchResult: Flow<PagingData<HotelOffer>>? = null

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val error = SingleLiveEvent<String>()

    private lateinit var checkInDate: String
    private lateinit var checkOutDate: String

    fun searchByDestination(
        destination: String,
        checkInDate: LocalDate,
        checkOutDate: LocalDate
    ): Flow<PagingData<HotelOffer>> {
        val newCheckInDate = checkInDate.toString()
        val newCheckOutDate = checkOutDate.toString()
        val lastResult = currentSearchResult

        if (destination == currentDestination
            && newCheckInDate == this.checkInDate
            && newCheckOutDate == this.checkOutDate
            && lastResult != null
        ) {
            return lastResult
        }

        currentDestination = destination
        this.checkInDate = checkInDate.toString()
        this.checkOutDate = checkOutDate.toString()

        val newResult: Flow<PagingData<HotelOffer>> = getSearchResultStream(
            destination,
            this.checkInDate,
            this.checkOutDate
        ).cachedIn(viewModelScope)

        currentSearchResult = newResult
        return newResult
    }

    private fun getSearchResultStream(
        destination: String,
        checkInDate: String,
        checkOutDate: String
    ): Flow<PagingData<HotelOffer>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                HotelsOffersPagingSource(destination, checkInDate, checkOutDate)
            }
        ).flow
    }

    fun onHotelOfferClick(view: View, hotelOffer: HotelOffer) {
        try {
            view.findNavController().navigate(
                R.id.ratesFragment,
                RatesFragmentArgs(
                    hotelOffer.hotel?.hotelId ?: "",
                    checkInDate,
                    checkOutDate
                ).toBundle()
            )
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
