package com.arbonik.exchangerates

sealed class LoadState {
    object Loading : LoadState()
    class Error(val exception: Throwable) : LoadState()
    object Success : LoadState()
}