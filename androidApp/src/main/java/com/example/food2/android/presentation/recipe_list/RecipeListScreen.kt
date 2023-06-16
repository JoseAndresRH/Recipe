package com.example.food2.android.presentation.recipe_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.example.food2.android.presentation.recipe_list.components.SearchAppBar
import com.example.food2.android.presentation.theme.AppTheme
import com.example.food2.presentation.recipe_list.RecipeListEvents
import com.example.food2.presentation.recipe_list.RecipeListState
import com.example.food2.android.presentation.recipe_list.components.RecipeList
import com.example.food2.presentation.recipe_list.FoodCategoryUtil

@ExperimentalComposeUiApi
@Composable
fun RecipeListScreen(
    state: RecipeListState,
    onTriggerEvent: (RecipeListEvents) -> Unit,
    onclickRecipeListItem: (Int) -> Unit
) {
    AppTheme(
        displayProgressBar = state.isLoading,
        dialogQueue = state.queue,
        onRemoveHeadMessageFromQueue = {
            onTriggerEvent(RecipeListEvents.OnRemoveHeadMessageFromQueue)
        }
    ) {
        val foodCategories = remember { FoodCategoryUtil().getAllFoodCategories()}
        Scaffold(
            topBar = {
                SearchAppBar(
                    query = state.query,
                    categories = foodCategories,
                    onSelectedCategoryChanged = {
                        onTriggerEvent(RecipeListEvents.OnSelectCategory(it))
                    },
                    selectedCategory = state.selectedCategory,
                    onQueryChanged = {
                        onTriggerEvent(RecipeListEvents.OnUpdateQuery(it))
                    },
                    onExecuteSearch = {
                        onTriggerEvent(RecipeListEvents.NewSearch)
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    RecipeList(
                        loading = state.isLoading,
                        recipes = state.recipes,
                        page = state.page,
                        onTriggerNextPage = {
                            onTriggerEvent(RecipeListEvents.NextPage)
                        },
                        onClickRecipeListItem = onclickRecipeListItem
                    )
                }
            }
        )
    }
}