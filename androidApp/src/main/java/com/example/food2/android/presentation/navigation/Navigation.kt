package com.example.food2.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food2.android.presentation.recipe_detail.RecipeDetailScreen
import com.example.food2.android.presentation.recipe_detail.RecipeDetailViewModel
import com.example.food2.android.presentation.recipe_list.RecipeListScreen
import com.example.food2.android.presentation.recipe_list.RecipeListViewModel

@ExperimentalComposeUiApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.RecipeList.route) {
        composable(route = Screen.RecipeList.route) { navBackStackEntry ->
            val viewModel: RecipeListViewModel = hiltViewModel()
            RecipeListScreen(
                state = viewModel.state.value,
                onTriggerEvent = viewModel::onTriggerEvent,
                onclickRecipeListItem = { recipeId ->
                navController.navigate(Screen.RecipeDetail.route + "/$recipeId")
            }
            )
        }
        composable(
            route = Screen.RecipeDetail.route + "/{recipeId}",
            arguments = listOf(navArgument("recipeId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
        //    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
        // val   viewModel: RecipeDetailViewModel = viewModel(key="RecipeDetailViewModel", factory=factory)
            val viewModel: RecipeDetailViewModel = hiltViewModel()

            RecipeDetailScreen(
                state = viewModel.state.value,
                onTriggerEvent = viewModel::onTriggerEvent
            )
        }
    }
}