package com.amadeus.android.demo.utils

import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult.Success


fun Success<*>.hasFirst() = hasMeta(Amadeus.FIRST)

fun Success<*>.hasLast() = hasMeta(Amadeus.LAST)

fun Success<*>.hasNext() = hasMeta(Amadeus.NEXT)

fun Success<*>.hasPrevious() = hasMeta(Amadeus.PREVIOUS)

fun Success<*>.hasMeta(key: String) = meta?.links?.get(key) != null