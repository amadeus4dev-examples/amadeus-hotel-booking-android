package com.amadeus.android.demo.utils

data class DisplayableElement<T>(
    val type: Type,
    val element: T?
) {
    enum class Type {
        ELEMENT, LOAD_MORE
    }

    companion object {

        inline fun <reified T> from(element: T): DisplayableElement<T> {
            return DisplayableElement(Type.ELEMENT, element)
        }

        inline fun <reified T> newLoadMore(): DisplayableElement<T> {
            return DisplayableElement(Type.LOAD_MORE, null)
        }

    }
}

