package com.example.food2.di

import com.example.food2.datasource.network.KtorClientFactory
import com.example.food2.datasource.network.RecipeService
import com.example.food2.datasource.network.RecipeServiceImpl

class NetworkModule {

    val recipeService: RecipeService by lazy {
        RecipeServiceImpl(
            httpClient = KtorClientFactory().build(),
            baseUrl = RecipeServiceImpl.BASE_URL
        )
    }
}