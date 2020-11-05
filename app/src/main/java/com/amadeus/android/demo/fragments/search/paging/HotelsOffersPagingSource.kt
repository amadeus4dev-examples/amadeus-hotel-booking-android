package com.amadeus.android.demo.fragments.search.paging

import androidx.paging.PagingSource
import com.amadeus.android.ApiResult
import com.amadeus.android.demo.SampleApplication
import com.amadeus.android.domain.resources.HotelOffer

class HotelsOffersPagingSource(
    private val destination: String,
    private val checkInDate: String,
    private val checkOutDate: String
) : PagingSource<ApiResult.Success<List<HotelOffer>>, HotelOffer>() {

    private val amadeus = SampleApplication.amadeus

    override suspend fun load(params: LoadParams<ApiResult.Success<List<HotelOffer>>>): LoadResult<ApiResult.Success<List<HotelOffer>>, HotelOffer> {
        val key = params.key
        val response = when {
            params is LoadParams.Append && key != null -> amadeus.next(key)
            params is LoadParams.Prepend && key != null -> amadeus.previous(key)
            else -> amadeus.shopping.hotelOffers.get(
                cityCode = destination,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate
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