package com.kdjj.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kdjj.presentation.model.RecipeEditorItem

class RecipeEditorItemCallback : DiffUtil.ItemCallback<RecipeEditorItem>() {

    override fun areItemsTheSame(
        oldItem: RecipeEditorItem,
        newItem: RecipeEditorItem
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: RecipeEditorItem,
        newItem: RecipeEditorItem
    ): Boolean = true
}