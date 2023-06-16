package com.example.food2.di

import com.example.food2.datasource.cache.*
import com.example.food2.util.DatetimeUtil

class CacheModule {

    private val driverFactory: DriverFactory by lazy {DriverFactory()}
    val recipeDatabase: RecipeDatabase by lazy{
        RecipeDatabaseFactory(driverFactory = driverFactory).createDatabase()
    }
    val recipeCache: RecipeCache by lazy {
        RecipeCacheImpl(
            recipeDatabase = recipeDatabase,
            datetimeUtil = DatetimeUtil()
        )
    }
}