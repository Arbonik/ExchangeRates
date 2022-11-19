package com.arbonik.exchangerates.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arbonik.exchangerates.entities.FavoriteExchange
import com.arbonik.exchangerates.network.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
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

@Dao
interface ExchangeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteExchange: FavoriteExchange)

    @Query("SELECT * FROM favorite")
    fun favorites(): Flow<List<FavoriteExchange>>

}

@Database(entities = [FavoriteExchange::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeDao(): ExchangeDao
}