package com.kdjj.presentation.model

import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.kdjj.domain.model.Recipe

internal sealed class MyRecipeItem {

    data class MyRecipe(
        val recipe: Recipe,
    ) : MyRecipeItem()

    object PlusButton : MyRecipeItem()
}