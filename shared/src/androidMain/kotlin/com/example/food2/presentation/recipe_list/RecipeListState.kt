package com.example.food2.presentation.recipe_list

import com.example.food2.domain.model.GenericMessageInfo
import com.example.food2.domain.model.Recipe
import com.example.food2.util.Queue

actual data class RecipeListState(
    val isLoading: Boolean = false,
    val page: Int = 1,
    val query: String = "",
    val selectedCategory: FoodCategory? = null,
    val recipes: List<Recipe> = listOf(),
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()), // messages to be displayed in ui
)