package com.arbonik.exchangerates

import com.arbonik.exchangerates.di.ExchangeDao
import com.arbonik.exchangerates.entities.Exchange
import com.arbonik.exchangerates.entities.LatestRubResponse
import com.arbonik.exchangerates.network.KtorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val exchangeDao: ExchangeDao,
    private val ktorClient: KtorClient,
//    private val
) {
    suspend fun getExchanges(exchangeCode : String) :Result<List<Exchange>> {
        return ktorClient.getExchange(exchangeCode).map {
            it.rates.map { exchange ->
                Exchange(exchange.key, exchange.value)
            }
        }
    }

}