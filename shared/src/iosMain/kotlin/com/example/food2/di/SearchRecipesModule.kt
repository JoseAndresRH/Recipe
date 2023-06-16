package com.example.food2.di

import com.example.food2.interactors.recipe_list.SearchRecipes

class SearchRecipesModule(
    val networkModule: NetworkModule,
    val cacheModule: CacheModule,
) {

    val searchRecipes: SearchRecipes by lazy{
        SearchRecipes(
            recipeService = networkModule.recipeService,
            recipeCache = cacheModule.recipeCache
        )
    }

}