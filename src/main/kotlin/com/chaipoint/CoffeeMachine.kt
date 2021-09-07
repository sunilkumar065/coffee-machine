package com.chaipoint

import com.chaipoint.dao.RecipeDao
import com.chaipoint.model.BeverageRecipe
import com.chaipoint.model.Ingredient
import com.chaipoint.service.BeverageService
import com.chaipoint.service.InventoryService
import com.chaipoint.type.Beverage

class CoffeeMachine{
    private var inventoryService: InventoryService
    private var recipeDao: RecipeDao
    private var beverageService: BeverageService

    constructor(inventoryService: InventoryService, recipeDao: RecipeDao, noOfOutlets: Int) {
        this.inventoryService = inventoryService
        this.recipeDao = recipeDao
        this.beverageService = BeverageService(noOfOutlets)
    }

    fun acceptOrder(beverage: Beverage) {
        if (!this.beverageService.canTakeOrder()) {
            println("Cannot prepare order $beverage. Machine at full capacity")
            return
        }
        val drinkRecipe = this.recipeDao.getDrinkRecipe(beverage)
        inventoryService.getIngredientFromInventory(drinkRecipe.beverageIngredients)
        this.beverageService.prepareBeverage(drinkRecipe)
    }

    fun refillIngredient(ingredient: Ingredient, quantity: Double) {
        inventoryService.addIngredientToInventory(ingredient, quantity)
    }

    fun addNewRecipe(recipe: BeverageRecipe) {
        this.recipeDao.updateDrinkRecipe(recipe.beverage, recipe)
    }
}