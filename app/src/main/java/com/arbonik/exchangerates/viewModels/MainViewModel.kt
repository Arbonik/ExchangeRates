package com.arbonik.exchangerates.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbonik.exchangerates.ExchangeRepository
import com.arbonik.exchangerates.LoadState
import com.arbonik.exchangerates.entities.Exchange
import com.arbonik.exchangerates.entities.FavoriteExchangePair
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
) : ViewModel() {
    companion object {
        private const val defaultExchangeCode = "RUB"
    }

    fun setFavorite(exchange : Exchange, isFavorite : Boolean){
        viewModelScope.launch {
            if (isFavorite)
                exchangeRepository.makeFavorite(
                    FavoriteExchangePair(exchange.name, _selectedExchange.value)
                )
            else
                exchangeRepository.deleteFavorite(
                    FavoriteExchangePair(exchange.name, _selectedExchange.value)
                )
        }
    }

    // загруженные пары
    private val _exchanges: MutableStateFlow<List<Exchange>> =
        MutableStateFlow(emptyList())
    val exchanges: StateFlow<List<Exchange>> = _exchanges.combine(
        exchangeRepository.getFavoriteExchange()
    ){ all, favorite ->
        all.map { ex ->
            if (favorite.contains(FavoriteExchangePair(ex.name, selectedExchange.value)))
                ex.copy(isFavorite = true)
            else
                ex
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private val _loadState: MutableStateFlow<LoadState> =
        MutableStateFlow(LoadState.Loading)
    val loadState: StateFlow<LoadState> = _loadState.asStateFlow()

    private val exchangeCodes: StateFlow<List<String>> = _exchanges.map { loadState ->
        loadState.map { exchange ->
            exchange.name
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // текущая выбранная пара
    private val _selectedExchange: MutableStateFlow<String> = MutableStateFlow(defaultExchangeCode)
    val selectedExchange: StateFlow<String> = _selectedExchange.asStateFlow()

    // введенная строка пользователем
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
            _loadState.value = LoadState.Loading
            exchangeRepository.getExchanges(exchangeCode)
                .onFailure {
                    _loadState.value = LoadState.Error(it)
                }.onSuccess {
                    _selectedExchange.value = exchangeCode
                    _loadState.value = LoadState.Success
                    _exchanges.value = it
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