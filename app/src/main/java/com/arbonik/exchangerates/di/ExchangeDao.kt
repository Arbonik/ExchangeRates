package com.arbonik.exchangerates.di

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arbonik.exchangerates.entities.FavoriteExchangePair
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteExchange: FavoriteExchangePair)

    @Query("SELECT * FROM favoriteExchange")
    fun favoritesExchange(): Flow<List<FavoriteExchangePair>>

    @Delete
    fun deleteFavorite(exchange: FavoriteExchangePair)
}