package com.example.food2.interactors.recipe_detail

import com.example.food2.datasource.cache.RecipeCache
import com.example.food2.domain.model.GenericMessageInfo
import com.example.food2.domain.model.Recipe
import com.example.food2.interactors.recipe_list.UIComponentType
import com.example.food2.util.CommonFlow
import com.example.food2.util.DataState
import com.example.food2.util.asCommonFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeCache: RecipeCache
) {
    fun execute(
        recipeId: Int
    ): CommonFlow<DataState<Recipe>> = flow {

        try {
            emit(DataState.loading())

            delay(2000)

            val recipe = recipeCache.get(recipeId)

            emit(DataState.data(message = null, data = recipe))
        } catch (e: Exception){
            emit(DataState.error(
                message = GenericMessageInfo.Builder()
                    .id("GetRecipe.Error")
                    .title("Error")
                    .uiComponentType(UIComponentType.Dialog)
                    .description(e.message?: "Unknown Error")
            ))
        }
    }.asCommonFlow()
}