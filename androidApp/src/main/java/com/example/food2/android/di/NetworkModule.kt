package com.example.food2.android.di

import com.example.food2.datasource.network.KtorClientFactory
import com.example.food2.datasource.network.RecipeService
import com.example.food2.datasource.network.RecipeServiceImpl
import com.example.food2.datasource.network.RecipeServiceImpl.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providerHttpClient(): HttpClient {
        return KtorClientFactory().build()
    }

    @Singleton
    @Provides
    fun provideRecipeService(
        httpClient: HttpClient
    ) : RecipeService {
        return RecipeServiceImpl(
            httpClient = httpClient,
            baseUrl = BASE_URL
        )
    }
}