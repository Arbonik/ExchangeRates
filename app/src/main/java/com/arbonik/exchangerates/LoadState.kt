package com.arbonik.exchangerates

sealed class LoadState<out T> {
    object Loading : LoadState<Nothing>()
    class Error(val exception: Throwable) : LoadState<Nothing>()
    class Success<T>(val data: T) : LoadState<T>()
}