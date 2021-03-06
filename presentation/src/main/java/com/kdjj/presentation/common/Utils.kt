package com.kdjj.presentation.common

import com.kdjj.domain.model.Recipe


const val RECIPE_ID = "recipeID"
const val RECIPE_STATE = "recipeState"
const val UPDATED_RECIPE_ID = "updatedRecipeId"
const val ACTION_START = "ACTION_START"

internal fun calculateSeconds(min: Int, sec: Int): Int {
    return min * 60 + sec
}

internal fun calculateTotalTime(recipe: Recipe?): Int {
    return recipe?.let { it.stepList.map { it.seconds }.fold(0) { acc, i -> acc + i } } ?: 0
}
