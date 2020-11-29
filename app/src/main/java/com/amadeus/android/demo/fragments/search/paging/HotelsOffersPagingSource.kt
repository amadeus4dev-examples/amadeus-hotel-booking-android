package com.amadeus.android.demo.fragments.search.paging

import androidx.paging.PagingSource
import com.amadeus.android.ApiResult
import com.amadeus.android.demo.SampleApplication
import com.amadeus.android.demo.fragments.search.HotelsOffersViewModel
import com.amadeus.android.domain.resources.HotelOffer

class HotelsOffersPagingSource(
    private val destination: String,
    private val checkInDate: String,
    private val checkOutDate: String
) : PagingSource<ApiResult.Success<List<HotelOffer>>, HotelOffer>() {

    private val amadeus = SampleApplication.amadeus

    override suspend fun load(params: LoadParams<ApiResult.Success<List<HotelOffer>>>): LoadResult<ApiResult.Success<List<HotelOffer>>, HotelOffer> {
        val key: ApiResult.Success<List<HotelOffer>>? = params.key
        val response = when {
            // We should load next page
            params is LoadParams.Append && key != null -> amadeus.next(key)
            // We should load previous page
            params is LoadParams.Prepend && key != null -> amadeus.previous(key)
            // We should refresh using `amadeus.self()` only if key is not null
            params is LoadParams.Refresh && key != null -> amadeus.self(key)
            // Key is null, it's a cold start for the lib, we should query the api
            else -> amadeus.shopping.hotelOffers.get(
                cityCode = destination,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                // We use the same page size set in PageConfig
                pageLimit = HotelsOffersViewModel.NETWORK_PAGE_SIZE
            )
        }
        return when (response) {
            is ApiResult.Success -> LoadResult.Page(
                data = response.data,
                prevKey = if (response.hasPrevious()) response else null,
                nextKey = if (response.hasNext()) response else null
            )
            else -> LoadResult.Error(IllegalStateException("Impossible to load requested page"))
        }
    }

}