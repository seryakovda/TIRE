package com.example.tire.di

import android.content.Context


import androidx.room.Room
import androidx.work.WorkManager
import com.example.tire.data.DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            this.engine {
                this.requestTimeout = 30000L
            }
        }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): DB = Room.databaseBuilder(
        context.applicationContext,
        DB::class.java,
        "todo_list_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}