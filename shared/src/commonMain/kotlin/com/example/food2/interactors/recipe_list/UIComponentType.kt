package com.example.food2.interactors.recipe_list

sealed class UIComponentType {
    object Dialog: UIComponentType()

    object None: UIComponentType()
}
