package com.chaipoint.dao

import com.chaipoint.exception.IngredientDoesNotExistException
import com.chaipoint.model.Ingredient

class InventoryDao {
    companion object {
        private var ingredientQuantityMap = mutableMapOf<Ingredient, Double>()
    }

    constructor(ingredientQuantityMap: MutableMap<Ingredient, Double>) {
        InventoryDao.ingredientQuantityMap = ingredientQuantityMap
    }

    fun getIngredientQuantity(ingredient: Ingredient): Double {
        val quantity = ingredientQuantityMap[ingredient]
        if(quantity == null) {
            throw IngredientDoesNotExistException("Ingredient ${ingredient.name} does not exists")
        } else {
            return quantity
        }
    }

    fun updateIngredient(ingredient: Ingredient, updatedQuantity: Double) {
        ingredientQuantityMap[ingredient] = updatedQuantity
    }

    fun printInventory() {
        ingredientQuantityMap.forEach { (ingredient, d) -> println("${ingredient.name} - $d")}
    }


}