package com.example.food2.di

import com.example.food2.interactors.recipe_detail.GetRecipe

class GetRecipeModule (
    private val cacheModule: CacheModule,
) {

    val getRecipe: GetRecipe by lazy{
        GetRecipe(
            recipeCache = cacheModule.recipeCache
        )
    }
}