package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.databinding.ItemEditorAddStepBinding
import com.kdjj.presentation.databinding.ItemEditorRecipeMetaBinding
import com.kdjj.presentation.databinding.ItemEditorRecipeStepBinding
import com.kdjj.presentation.model.RecipeEditorItem

class RecipeEditorListAdapter :
    ListAdapter<RecipeEditorItem, RecyclerView.ViewHolder>(RecipeEditorItemCallback()) {

    class RecipeMetaViewHolder(private val binding: ItemEditorRecipeMetaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecipeEditorItem.RecipeMeta) {

        }
    }

    class RecipeStepViewHolder(private val binding: ItemEditorRecipeStepBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecipeEditorItem.RecipeStep) {

        }
    }

    class AddStepViewHolder(private val binding: ItemEditorAddStepBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {

        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is RecipeEditorItem.RecipeMeta -> TYPE_META
            is RecipeEditorItem.RecipeStep -> TYPE_STEP
            is RecipeEditorItem.AddStep -> TYPE_ADD
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_META -> {
                val binding = ItemEditorRecipeMetaBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecipeMetaViewHolder(binding)
            }
            TYPE_STEP -> {
                val binding = ItemEditorRecipeStepBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecipeStepViewHolder(binding)
            }
            else -> {
                val binding = ItemEditorAddStepBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddStepViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (item as? RecipeEditorItem.RecipeMeta)?.let { (holder as RecipeMetaViewHolder).bind(item) }
            ?: (item as? RecipeEditorItem.RecipeStep)?.let { (holder as RecipeStepViewHolder).bind(item) }
            ?: (item as? RecipeEditorItem.AddStep)?.let { (holder as AddStepViewHolder).bind() }
    }

    companion object {
        private const val TYPE_META = 0
        private const val TYPE_STEP = 1
        private const val TYPE_ADD = 2
    }
}