package com.example.food2.presentation.recipe_detail

import com.example.food2.domain.model.GenericMessageInfo
import com.example.food2.domain.model.Recipe
import com.example.food2.util.Queue

data class RecipeDetailState(
    val isLoading: Boolean = false,
    val recipe: Recipe? = null,
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf())
){
    // Need secondary constructor to initialize with no args in SwiftUI
    constructor(): this(
        isLoading = false,
        recipe = null,
        queue = Queue(mutableListOf()),
    )
}
