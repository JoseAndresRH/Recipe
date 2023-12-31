package com.example.food2.android.presentation.recipe_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food2.domain.model.GenericMessageInfo
import com.example.food2.domain.model.NegativeAction
import com.example.food2.domain.model.PositiveAction
import com.example.food2.domain.model.Recipe
import com.example.food2.presentation.recipe_list.RecipeListEvents
import com.example.food2.interactors.recipe_list.SearchRecipes
import com.example.food2.interactors.recipe_list.UIComponentType
import com.example.food2.presentation.recipe_list.FoodCategory
import com.example.food2.presentation.recipe_list.RecipeListState
import com.example.food2.util.DataState
import com.example.food2.util.GenericMessageInfoQueueUtil
import com.example.food2.util.Queue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle, // don't need for this VM
    private val searchRecipes: SearchRecipes,
) : ViewModel() {
    val state: MutableState<RecipeListState> = mutableStateOf(RecipeListState())

    init {
        onTriggerEvent(RecipeListEvents.LoadRecipes)

        // EXAMPLE
//        val messageInfoBuilder = GenericMessageInfo.Builder()
//            .id(UUID.randomUUID().toString())
//            .title("Weird")
//            .uiComponentType(UIComponentType.Dialog)
//            .description("I don't know what's happening.")
//            .positive(PositiveAction(
//                positiveBtnTxt = "OK",
//                onPositiveAction = {
//                    // do something special??
//                    state.value = state.value.copy(query = "Kale")
//                    onTriggerEvent(RecipeListEvents.NewSearch)
//                }
//            ))
//            .negative(NegativeAction(
//                negativeBtnTxt = "Cancel",
//                onNegativeAction = {
//                    state.value = state.value.copy(query = "Cookies")
//                    onTriggerEvent(RecipeListEvents.NewSearch)
//                }
//            ))
//        appendToMessageQueue(messageInfo = messageInfoBuilder)
  //  }
    }

    fun onTriggerEvent(event: RecipeListEvents) {
        when (event) {
            RecipeListEvents.LoadRecipes -> {
                loadRecipes()
            }
            RecipeListEvents.NextPage -> {
                nextPage()
            }
            RecipeListEvents.NewSearch -> {
                newSearch()
            }
            is RecipeListEvents.OnUpdateQuery -> {
                state.value = state.value.copy(query = event.query, selectedCategory = null)
            }
            is RecipeListEvents.OnSelectCategory -> {
                onSelectCategory(event.category)
            }
            is RecipeListEvents.OnRemoveHeadMessageFromQueue -> {
                removeHeadMessage()
            }
            else -> {
                appendToMessageQueue(
                    GenericMessageInfo.Builder()
                        .id(UUID.randomUUID().toString())
                        .title("Error")
                        .uiComponentType(UIComponentType.Dialog)
                        .description("Invalid Event")
                )
            }
        }
    }

    private fun onSelectCategory(category: FoodCategory) {
        state.value = state.value.copy(selectedCategory = category, query = category.value)
        newSearch()
    }

    private fun newSearch() {
        state.value = state.value.copy(page = 1, recipes = listOf())
        loadRecipes()
    }

    /**
     * Get the next page of recipes
     */
    private fun nextPage() {
        state.value = state.value.copy(page = state.value.page + 1)
        loadRecipes()
    }

    private fun loadRecipes() {
        searchRecipes.execute(
            page = state.value.page,
            query = state.value.query,
        ).collectCommon(viewModelScope) { dataState ->
            state.value = state.value.copy(isLoading = dataState.isLoading)

            dataState.data?.let { recipes ->
                appendRecipes(recipes)
            }

            dataState.message?.let { message ->
                val messageInfoBuilder = GenericMessageInfo.Builder()
                    .id(UUID.randomUUID().toString())
                    .title("Weird")
                    .uiComponentType(UIComponentType.Dialog)
                    .description("I don't know what's happening.")
                    .positive(PositiveAction(
                        positiveBtnTxt = "OK",
                        onPositiveAction = {
                            // do something special??
                            state.value = state.value.copy(query = "Kale")
                            onTriggerEvent(RecipeListEvents.NewSearch)
                        }
                    ))
                    .negative(NegativeAction(
                        negativeBtnTxt = "Cancel",
                        onNegativeAction = {
                            state.value = state.value.copy(query = "Cookies")
                            onTriggerEvent(RecipeListEvents.NewSearch)
                        }
                    ))
                appendToMessageQueue(messageInfo = messageInfoBuilder)
            }
        }
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val curr = ArrayList(state.value.recipes)
        curr.addAll(recipes)
        state.value = state.value.copy(recipes = curr)
    }

    private fun appendToMessageQueue(messageInfo: GenericMessageInfo.Builder) {
        if (!GenericMessageInfoQueueUtil().doesMessageAlreadyExistInQueue(
                queue = state.value.queue,
                messageInfo = messageInfo.build()
            )
        ) {
            val queue = state.value.queue
            queue.add(messageInfo.build())
            state.value = state.value.copy(queue = queue)
        }
    }

    private fun removeHeadMessage() {
        try {
            val queue = state.value.queue
            queue.remove() // can throw exception if empty
            state.value = state.value.copy(queue = Queue(mutableListOf())) // force recompose
            state.value = state.value.copy(queue = queue)
        }catch (e: Exception){
            // nothing to remove, queue is empty
        }
    }
}