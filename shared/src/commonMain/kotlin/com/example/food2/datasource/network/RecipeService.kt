package com.example.food2.datasource.network

import com.example.food2.domain.model.Recipe

interface RecipeService {

    suspend fun search(
        page: Int,
        query: String
    ): List<Recipe>

    suspend fun get(
        id: Int,
    ): Recipe
}
