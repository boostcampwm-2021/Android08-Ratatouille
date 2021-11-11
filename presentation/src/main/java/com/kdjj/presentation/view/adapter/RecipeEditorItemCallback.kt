package com.kdjj.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kdjj.presentation.model.RecipeEditorItem

class RecipeEditorItemCallback : DiffUtil.ItemCallback<RecipeEditorItem>() {

    override fun areItemsTheSame(
        oldItem: RecipeEditorItem,
        newItem: RecipeEditorItem
    ): Boolean = when {
        oldItem is RecipeEditorItem.RecipeMetaModel &&
                newItem is RecipeEditorItem.RecipeMetaModel ->
            oldItem.recipeId == newItem.recipeId
        oldItem is RecipeEditorItem.RecipeStepModel &&
                newItem is RecipeEditorItem.RecipeStepModel ->
            oldItem.stepId == newItem.stepId
        oldItem is RecipeEditorItem.PlusButton && newItem is RecipeEditorItem.PlusButton -> true
        else -> false
    }

    override fun areContentsTheSame(
        oldItem: RecipeEditorItem,
        newItem: RecipeEditorItem
    ): Boolean = true
}