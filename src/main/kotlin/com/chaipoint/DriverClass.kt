package com.chaipoint

import com.chaipoint.dao.InventoryDao
import com.chaipoint.dao.RecipeDao
import com.chaipoint.exception.IngredientDoesNotExistException
import com.chaipoint.exception.OrderQueueFullException
import com.chaipoint.model.BeverageIngredient
import com.chaipoint.model.BeverageRecipe
import com.chaipoint.model.Ingredient
import com.chaipoint.service.BeverageService
import com.chaipoint.service.InventoryService
import com.chaipoint.type.Beverage
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    val outlet = 3
    val hotWater = Ingredient("hot-water")
    val hotMilk = Ingredient("hot-milk")
    val gingerSyrup = Ingredient("ginger-syrup")
    val sugarSyrup = Ingredient("sugar-syrup")
    val teaLeaveSyrup = Ingredient("tea-leaves-syrup")
    val greenMixture = Ingredient("green-mixture")

    val ingredientQuantityMap = mutableMapOf(
        hotWater to 500.0,
        hotMilk to 500.0,
        gingerSyrup to 100.0,
        sugarSyrup to 100.0,
        teaLeaveSyrup to 100.0
    )

    val beverageRecipeList = mutableListOf<BeverageRecipe>()

    val hotTeaRecipe =  BeverageRecipe(Beverage.HOT_TEA
        , listOf(BeverageIngredient(hotWater, 200.0),
            BeverageIngredient(hotMilk, 100.0),
            BeverageIngredient(gingerSyrup, 10.0),
            BeverageIngredient(sugarSyrup, 10.0),
            BeverageIngredient(teaLeaveSyrup, 30.0)
        )
    )
    val hotCoffeeRecipe = BeverageRecipe(Beverage.HOT_COFFEE
        , listOf(BeverageIngredient(hotWater, 100.0),
            BeverageIngredient(hotMilk, 400.0),
            BeverageIngredient(gingerSyrup, 30.0),
            BeverageIngredient(sugarSyrup, 50.0),
            BeverageIngredient(teaLeaveSyrup, 30.0)
        )
    )

    val blackTeaRecipe = BeverageRecipe(Beverage.BLACK_TEA
        , listOf(BeverageIngredient(hotWater, 300.0),
            BeverageIngredient(gingerSyrup, 30.0),
            BeverageIngredient(sugarSyrup, 50.0),
            BeverageIngredient(teaLeaveSyrup, 30.0)
        )
    )

    val greenTeaRecipe = BeverageRecipe(Beverage.GREEN_TEA
        , listOf(BeverageIngredient(hotWater, 100.0),
            BeverageIngredient(gingerSyrup, 30.0),
            BeverageIngredient(sugarSyrup, 50.0),
            BeverageIngredient(greenMixture, 30.0)
        )
    )

    beverageRecipeList.add(hotTeaRecipe)
    beverageRecipeList.add(hotCoffeeRecipe)
    beverageRecipeList.add(greenTeaRecipe)
    beverageRecipeList.add(blackTeaRecipe)

    val inventoryDao = InventoryDao(ingredientQuantityMap)
    val inventoryService = InventoryService(inventoryDao)
    val recipeDao = RecipeDao(beverageRecipeList)

    val coffeeMachine = CoffeeMachine(inventoryService, recipeDao, outlet)

    // take orders
    coffeeMachine.acceptOrder(Beverage.HOT_COFFEE)
    coffeeMachine.acceptOrder(Beverage.HOT_TEA)

    try {
        coffeeMachine.acceptOrder(Beverage.GREEN_TEA)
    } catch (e: IngredientDoesNotExistException) {
        println("Cannot prepare ${Beverage.GREEN_TEA} because ${e.localizedMessage}")
    }

    try {
        coffeeMachine.acceptOrder(Beverage.BLACK_TEA)
    } catch (e: IngredientDoesNotExistException) {
        println("Cannot prepare ${Beverage.BLACK_TEA} because ${e.localizedMessage}")
    }


    // max concurrent orders
    val beverageService = BeverageService(outlet)
    val executorService = Executors.newFixedThreadPool(outlet)
    try {
        executorService.submit { beverageService.prepareBeverage(hotCoffeeRecipe) }
        executorService.submit { beverageService.prepareBeverage(hotTeaRecipe) }
        executorService.submit { beverageService.prepareBeverage(greenTeaRecipe) }
        executorService.submit { beverageService.prepareBeverage(blackTeaRecipe) }
    } catch (e: OrderQueueFullException) {
        println(e.localizedMessage)
    } finally {
        executorService.shutdown()
    }
}