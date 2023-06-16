//
//  RecipeListScreen.swift
//  iosApp
//
//  Created by Jose Andres Rodriguez Hernandez on 13/06/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct RecipeListScreen: View {
    
    // dependencies
    private let networkModule: NetworkModule
    private let cacheModule: CacheModule
    private let searchRecipesModule: SearchRecipesModule
    private let foodCategories: [FoodCategory]
    
    @ObservedObject var viewModel: RecipeListViewModel
    
    
    init(
        networkModule: NetworkModule,
        cacheModule: CacheModule
    ) {
        self.networkModule = networkModule
        self.cacheModule = cacheModule
        self.searchRecipesModule = SearchRecipesModule(
            networkModule: self.networkModule,
            cacheModule: self.cacheModule
        )
        let foodCategoryUtil = FoodCategoryUtil()
        self.viewModel = RecipeListViewModel(
            searchRecipes: searchRecipesModule.searchRecipes,
            foodCategoryUtil: foodCategoryUtil
        )
        self.foodCategories = foodCategoryUtil.getAllFoodCategories()
    }
    
    var body: some View {
        NavigationView{
            ZStack{
                VStack{
                    SearchAppBar(
                        query: viewModel.state.query,
                        selectedCategory: viewModel.state.selectedCategory,
                        foodCategories: foodCategories,
                        onTriggerEvent: { event in
                            viewModel.onTriggerEvent(stateEvent: event)
                        }
                    )
                    List {
                        ForEach(viewModel.state.recipes, id: \.self.id){ recipe in
                            VStack{
                                RecipeCard(recipe: recipe)
                                    .onAppear(perform: {
                                        if viewModel.shouldQueryNextPage(recipe: recipe){
                                            viewModel.onTriggerEvent(stateEvent: RecipeListEvents.NextPage())
                                        }
                                    })
                                    .overlay(NavigationLink(
                                        destination: RecipeDetailScreen(
                                            recipeId: Int(recipe.id),
                                            cacheModule: self.cacheModule
                                        ), label: {
                                            EmptyView()
                                        }))
                            }
                            
                        }
                        .listRowInsets(EdgeInsets())
                        .padding(.top, 10)
                    }
                }
                .listStyle(PlainListStyle())
            }
            if viewModel.state.isLoading {
                ProgressView("Searching recipes...")
            }
        }
        .navigationBarBackButtonHidden(true)
        .alert(isPresented: $viewModel.showDialog, content: {
            let first = viewModel.state.queue.peek()!
            return GenericMessageInfoAlert().build(
                message: first,
                onRemoveHeadMessage: {
                    viewModel.onTriggerEvent(stateEvent: RecipeListEvents.OnRemoveHeadMessageFromQueue())
                }
            )
        })
    }
}
//struct RecipeListScreen_Previews: PreviewProvider {
//    static var previews: some View {
//        RecipeListScreen()
//    }
//}
