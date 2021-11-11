package com.kdjj.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.model.RecipeEditorItem

internal class MyRecipeItemCallback : DiffUtil.ItemCallback<MyRecipeItem>() {
    override fun areItemsTheSame(oldItem: MyRecipeItem, newItem: MyRecipeItem): Boolean {
        return when {
            oldItem is MyRecipeItem.MyRecipe && newItem is MyRecipeItem.MyRecipe -> {
                oldItem.recipe.recipeId == newItem.recipe.recipeId
            }
            oldItem is MyRecipeItem.PlusButton && newItem is MyRecipeItem.PlusButton -> true
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: MyRecipeItem, newItem: MyRecipeItem): Boolean {
        return when {
            oldItem is MyRecipeItem.MyRecipe && newItem is MyRecipeItem.MyRecipe -> {
                oldItem.recipe.createTime == newItem.recipe.createTime
            }
            oldItem is MyRecipeItem.PlusButton && newItem is MyRecipeItem.PlusButton -> true
            else -> false
        }
    }
}