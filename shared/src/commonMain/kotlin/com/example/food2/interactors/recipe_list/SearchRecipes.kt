package com.example.food2.interactors.recipe_list

import com.example.food2.datasource.cache.RecipeCache
import com.example.food2.datasource.network.RecipeService
import com.example.food2.domain.model.GenericMessageInfo
import com.example.food2.domain.model.Recipe
import com.example.food2.util.CommonFlow
import com.example.food2.util.DataState
import com.example.food2.util.asCommonFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRecipes(
    private val recipeService: RecipeService,
    private val recipeCache: RecipeCache
) {
    fun execute(
        page: Int,
        query: String
    ): CommonFlow<DataState<List<Recipe>>> = flow {
        // how can we emit loading
        emit(DataState.loading())

        // emit recipes
        try {
            val recipes = recipeService.search(
                page = page,
                query = query
            )
            delay(50)

            if(query == "error"){
                throw Exception("Forcing an error... SearchRecipe")
            }

            recipeCache.insert(recipes)

            val cacheResult = if(query.isBlank()){
               recipeCache.getAll(page = page)
            }else{
                recipeCache.search(
                    query = query,
                    page = page
                )
            }

            emit(DataState.data(message = null, data = cacheResult))
        } catch (e: Exception){
            // how can we emit error?
            emit(DataState.error(
                message = GenericMessageInfo.Builder()
                .id("SearchRecipes.Error")
                    .title("Error")
                    .uiComponentType(UIComponentType.Dialog)
                    .description(e.message?: "Unknown Error")
            ))
        }
    }.asCommonFlow()
}