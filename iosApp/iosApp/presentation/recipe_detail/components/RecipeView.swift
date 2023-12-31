//
//  RecipeView.swift
//  iosApp
//
//  Created by Jose Andres Rodriguez Hernandez on 13/06/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import SDWebImageSwiftUI
import shared

struct RecipeView: View {
    
    private let recipe: Recipe
    private let dateUtil: DatetimeUtil
    
    init(
        recipe: Recipe,
        dateUtil: DatetimeUtil
    ) {
        self.recipe = recipe
        self.dateUtil = dateUtil
    }
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading){
                WebImage(url: URL(string: recipe.featuredImage))
                            .resizable()
                            .placeholder(Image(systemName: "photo")) // Placeholder Image
                            .placeholder {
                                Rectangle().foregroundColor(.white)
                            }
                            .indicator(.activity)
                            .transition(.fade(duration: 0.5))
                            .scaledToFill() // 1
                            .frame(height: 250, alignment: .center) // 2
                            .clipped() // 3
                
                VStack(alignment: .leading){
                    
                    HStack(alignment: .lastTextBaseline){
                        Text(
                            "Updated \(dateUtil.humanizeDatetime(date: recipe.dateUpdated)) by \(recipe.publisher)"
                        )
                        .foregroundColor(Color.gray)

                        Spacer()
                        
                        Text(String(recipe.rating))
                            .frame(alignment: .trailing)
                    }
                    
                    ForEach(recipe.ingredients as Array<String>, id: \.self){ ingredient in
                        Text(ingredient)
                            .padding(.top, 4)
                    }
                }
                .background(Color.white)
                .padding(12)
            }
            .buttonStyle(PlainButtonStyle())
        }
        .navigationBarTitle(Text(recipe.title), displayMode: .inline)
    }
}

//struct RecipeView_Previews: PreviewProvider {
//    static var previews: some View {
//        RecipeView()
//    }
//}
