package com.chaipoint.service

import com.chaipoint.dao.InventoryDao
import com.chaipoint.exception.IngredientDoesNotExistException
import com.chaipoint.model.BeverageIngredient
import com.chaipoint.model.Ingredient

class InventoryService(private val inventoryDao: InventoryDao) {
    fun getIngredientQuantity(ingredient: Ingredient): Double {
        return inventoryDao.getIngredientQuantity(ingredient)
    }

    fun addIngredientToInventory(ingredient: Ingredient, volume: Double) {
        val currentVolume = inventoryDao.getIngredientQuantity(ingredient)
        val newVolume = currentVolume + volume
        inventoryDao.updateIngredient(ingredient, newVolume)
    }

    @Synchronized
    fun getIngredientFromInventory(ingredientList: List<BeverageIngredient>) {
        ingredientList.forEach { drinkIngredient ->
            inventoryDao.getIngredientQuantity(drinkIngredient.ingredient)
        }

        ingredientList.forEach { drinkIngredient ->
            val currentQuantity = inventoryDao.getIngredientQuantity(drinkIngredient.ingredient)
            if (currentQuantity < drinkIngredient.quantity) {
                throw IngredientDoesNotExistException("Insufficient ingredient ${drinkIngredient.ingredient.name}")
            }
        }

        ingredientList.forEach { drinkIngredient ->
            var currentQuantity = inventoryDao.getIngredientQuantity(drinkIngredient.ingredient)
            currentQuantity -= drinkIngredient.quantity
            inventoryDao.updateIngredient(drinkIngredient.ingredient, currentQuantity)
        }
    }

    private fun alert(ingredient: BeverageIngredient) {
        println("Ingredient ${ingredient.ingredient.name} low in quantity")
    }

}