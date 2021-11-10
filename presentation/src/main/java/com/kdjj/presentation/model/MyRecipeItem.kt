package com.kdjj.presentation.model

import com.kdjj.domain.model.Recipe

sealed class MyRecipeItem {

    data class MyRecipe(
        val recipe: Recipe,
    ) : MyRecipeItem()

    object PlusButton : MyRecipeItem()
}