package com.arbonik.exchangerates.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class FavExchangePairWithCost(
    val favoriteExchangePair: FavoriteExchangePair,
    val cost : Double
)

@Entity(tableName = "favoriteExchange", primaryKeys = ["first", "second"])
data class FavoriteExchangePair(
    val first : String,
    val second : String
)
