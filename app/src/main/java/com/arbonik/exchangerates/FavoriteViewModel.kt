package com.arbonik.exchangerates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arbonik.exchangerates.entities.FavExchangePairWithCost
import com.arbonik.exchangerates.entities.FavoriteExchangePair
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
) : ViewModel() {

    private val favoriteExchange = exchangeRepository.getFavoriteExchange()

    val exchangeWithCost: StateFlow<List<StateFlow<ExchangeLoadingState>>> =
        favoriteExchange.map { list ->
            list.map { pair ->
                flow<ExchangeLoadingState> {
                    val actualCost =
                        exchangeRepository.getExchanges(pair.first).onFailure { exception ->
                            emit(ExchangeLoadingState.Failure(exception, pair))
                        }.getOrNull()
                            ?.find { exchange ->
                                exchange.name == pair.second
                            }?.cost
                    actualCost?.let {
                        emit(
                            ExchangeLoadingState.FromNetwork(
                                FavExchangePairWithCost(
                                    pair,
                                    actualCost
                                )
                            )
                        )
                    }
                }.stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    ExchangeLoadingState.FromDb(pair)
                )
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    fun deleteFavorite(exchange: FavoriteExchangePair) {
        viewModelScope.launch {
            exchangeRepository.deleteFavorite(
                FavoriteExchangePair(exchange.first, exchange.second)
            )
        }
    }
}

sealed class ExchangeLoadingState(
    val exchangePair: FavoriteExchangePair
) {
    class FromDb(exchangePair: FavoriteExchangePair) : ExchangeLoadingState(exchangePair)
    class FromNetwork(val exchangePairWithCost: FavExchangePairWithCost) :
        ExchangeLoadingState(exchangePairWithCost.favoriteExchangePair)

    class Failure(val exception: Throwable, exchangePair: FavoriteExchangePair) :
        ExchangeLoadingState(exchangePair)
}

