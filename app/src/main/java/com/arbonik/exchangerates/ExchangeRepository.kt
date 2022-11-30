package com.arbonik.exchangerates

import com.arbonik.exchangerates.di.ExchangeDao
import com.arbonik.exchangerates.entities.Exchange
import com.arbonik.exchangerates.entities.FavoriteExchangePair
import com.arbonik.exchangerates.network.KtorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val exchangeDao: ExchangeDao,
    private val ktorClient: KtorClient,
) {
    suspend fun getExchanges(exchangeCode : String) :Result<List<Exchange>> {
        return ktorClient.getExchange(exchangeCode).map {
            it.rates.map { exchange ->
                Exchange(exchange.key, exchange.value)
            }
        }
    }

    fun getFavoriteExchange(): Flow<List<FavoriteExchangePair>>{
        return exchangeDao.favoritesExchange()
    }

    suspend fun makeFavorite(exchange: FavoriteExchangePair){
        withContext(Dispatchers.IO) {
            exchangeDao.insertFavorite(exchange)
        }
    }

    suspend fun deleteFavorite(favoriteExchange: FavoriteExchangePair) {
        withContext(Dispatchers.IO) {
            exchangeDao.deleteFavorite(favoriteExchange)
        }
    }
}