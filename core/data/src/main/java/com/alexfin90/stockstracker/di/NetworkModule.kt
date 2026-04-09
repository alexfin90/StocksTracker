package com.alexfin90.stockstracker.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // No Timeout
        .pingInterval(30, TimeUnit.SECONDS) // keep-alive per il WebSocket
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}