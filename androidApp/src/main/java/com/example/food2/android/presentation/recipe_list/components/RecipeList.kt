package com.example.food2.android.presentation.recipe_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.food2.android.presentation.components.RECIPE_IMAGE_HEIGHT
import com.example.food2.datasource.network.RecipeServiceImpl.Companion.RECIPE_PAGINATION_PAGE_SIZE
import com.example.food2.domain.model.Recipe

@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    page: Int,
    onTriggerNextPage: () -> Unit,
    onClickRecipeListItem: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
    ) {
        if (loading && recipes.isEmpty()) {
            LoadingRecipeListShimmer(imageHeight = RECIPE_IMAGE_HEIGHT.dp)
        } else if (recipes.isEmpty()) {
            // nothing to show... No recipes
        } else {
            LazyColumn {
                itemsIndexed(
                    items = recipes
                ) { index, recipe ->
                    if ((index + 1) >= (page * RECIPE_PAGINATION_PAGE_SIZE) && !loading) {
                        onTriggerNextPage()
                    }
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            onClickRecipeListItem(recipe.id)
                        })
                }
            }
        }
    }
}