package com.arbonik.exchangerates

import com.arbonik.exchangerates.di.ExchangeDao
import com.arbonik.exchangerates.entities.Exchange
import com.arbonik.exchangerates.network.KtorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepository @Inject constructor(
    private val exchangeDao: ExchangeDao,
    private val ktorClient: KtorClient,
//    private val
) {
    suspend fun getExchanges() = withContext(Dispatchers.IO) {
        ktorClient.getExchange().rates.map { exchange ->
            Exchange(exchange.key, exchange.value)
        }
    }


}