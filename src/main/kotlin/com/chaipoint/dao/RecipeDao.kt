package com.chaipoint.dao

import com.chaipoint.model.BeverageRecipe
import com.chaipoint.type.Beverage

class RecipeDao {
    companion object {
        private val beverageRecipeMap = mutableMapOf<Beverage, BeverageRecipe>()
    }

    constructor(beverageRecipeList: MutableList<BeverageRecipe>) {
        beverageRecipeList.forEach { recipe -> beverageRecipeMap[recipe.beverage] = recipe }
    }

    fun getDrinkRecipe(beverage: Beverage): BeverageRecipe {
        val recipe = beverageRecipeMap[beverage]
        if(recipe == null) {
            throw RuntimeException("No recipe found for $beverage")
        } else {
            return recipe
        }
    }

    fun updateDrinkRecipe(beverage: Beverage, beverageRecipe: BeverageRecipe) {
        beverageRecipeMap[beverage] = beverageRecipe
    }
}