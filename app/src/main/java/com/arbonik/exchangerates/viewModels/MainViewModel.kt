package com.arbonik.exchangerates.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbonik.exchangerates.ExchangeRepository
import com.arbonik.exchangerates.entities.Exchange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
) : ViewModel() {

    private val _exchanges : MutableStateFlow<List<Exchange>> = MutableStateFlow(emptyList())
    val exchanges : StateFlow<List<Exchange>> = _exchanges.asStateFlow()

    fun loadExchanges(){
        viewModelScope.launch {
            _exchanges.value = exchangeRepository.getExchanges()
        }
    }
}