package com.kdjj.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.model.RecipeEditorItem

class MyRecipeItemCallback : DiffUtil.ItemCallback<MyRecipeItem.MyRecipe>() {
    override fun areItemsTheSame(
        oldItem: MyRecipeItem.MyRecipe,
        newItem: MyRecipeItem.MyRecipe
    ): Boolean {
        return oldItem.recipe.recipeId == newItem.recipe.recipeId
    }

    override fun areContentsTheSame(
        oldItem: MyRecipeItem.MyRecipe,
        newItem: MyRecipeItem.MyRecipe
    ): Boolean {
        return oldItem.recipe.createTime == newItem.recipe.createTime
    }
}
