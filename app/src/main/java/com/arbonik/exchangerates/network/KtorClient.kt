package com.arbonik.exchangerates.network

import com.arbonik.exchangerates.entities.LatestRubResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import java.net.URL

class KtorClient{
    //https://open.er-api.com/v6/latest/RUB
    private val client = HttpClient(CIO){
        install(ContentNegotiation){
            json()
        }
    }

    suspend fun getExchange():LatestRubResponse{
        val request = client.get(URL("https://open.er-api.com/v6/latest/RUB"))
        return request.body()
    }
}
