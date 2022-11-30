package com.arbonik.exchangerates.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arbonik.exchangerates.entities.FavoriteExchangePair

@Database(entities = [FavoriteExchangePair::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeDao(): ExchangeDao
}