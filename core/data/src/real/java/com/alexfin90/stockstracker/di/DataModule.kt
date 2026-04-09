package com.alexfin90.stockstracker.di

import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.repositories.StockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {


    @Singleton
    @Binds
    abstract fun bindStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository
}