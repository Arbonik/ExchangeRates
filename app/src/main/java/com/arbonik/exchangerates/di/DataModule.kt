package com.arbonik.exchangerates.di

import android.content.Context
import androidx.room.Room
import com.arbonik.exchangerates.network.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun databaseProvide(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "exchangeDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNetworkClient(): KtorClient {
        return KtorClient()
    }

    @Provides
    fun provideExchangeDao(
        database: AppDatabase
    ): ExchangeDao {
        return database.exchangeDao()
    }
}

