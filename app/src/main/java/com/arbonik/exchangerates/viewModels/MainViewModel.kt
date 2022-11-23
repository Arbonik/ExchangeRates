package com.arbonik.exchangerates.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbonik.exchangerates.ExchangeRepository
import com.arbonik.exchangerates.LoadState
import com.arbonik.exchangerates.entities.Exchange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
) : ViewModel() {
    companion object {
        private val defaultExchangeCode = "RUB"
    }

    // загруженные пары
    private val _exchanges: MutableStateFlow<LoadState<List<Exchange>>> =
        MutableStateFlow(LoadState.Success(emptyList()))
    val exchanges: StateFlow<LoadState<List<Exchange>>> = _exchanges.asStateFlow()

    val exchangeCodes: StateFlow<List<String>> = _exchanges.map { loadState ->
        if (loadState is LoadState.Success)
            loadState.data.map { exchange ->
                exchange.name
            }
        else emptyList()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // текущая выбранная пара
    private val _selectedExchange: MutableStateFlow<String> = MutableStateFlow(defaultExchangeCode)
    val selectedExchange: StateFlow<String> = _selectedExchange.asStateFlow()

    // вверденная строка пользователем
    private val _enteredExchange: MutableStateFlow<String> =
        MutableStateFlow(defaultExchangeCode)
    val enteredExchange: StateFlow<String> = _enteredExchange.asStateFlow()

    // результат поиска
    private val _searchExchanges: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val searchExchanges: StateFlow<List<String>> = _searchExchanges

    init {
        loadExchanges(defaultExchangeCode)
    }

    fun pickExchange(exchangeCode: String) {
        enterExchange(exchangeCode)
        loadExchanges(exchangeCode)
    }

    private fun loadExchanges(exchangeCode: String) {
        viewModelScope.launch {
            _exchanges.value = LoadState.Loading
            exchangeRepository.getExchanges(exchangeCode)
                .onFailure {
                    _exchanges.value = LoadState.Error(it)
                }.onSuccess {
                    _selectedExchange.value = exchangeCode
                    _exchanges.value = LoadState.Success(it)
                }
        }
    }

    fun enterExchange(enter: String) {
        _enteredExchange.value = enter
        if (enter != _selectedExchange.value) {
            _searchExchanges.value = exchangeCodes.value.filter { all ->
                all.startsWith(enter)
            }
        } else {
            _searchExchanges.value = emptyList()
        }
    }

    fun stopEnter(){
        _searchExchanges.value = emptyList()
    }
}