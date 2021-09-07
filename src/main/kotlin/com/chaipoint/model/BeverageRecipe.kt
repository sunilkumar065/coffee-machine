package com.chaipoint.model

import com.chaipoint.type.Beverage

data class BeverageRecipe(
    val beverage: Beverage,
    val beverageIngredients: List<BeverageIngredient>
)
