package com.arbonik.exchangerates.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteExchange(
    @PrimaryKey val name : String
)
