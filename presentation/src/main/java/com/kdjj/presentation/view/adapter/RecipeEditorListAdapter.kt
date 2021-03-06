package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ItemEditorAddStepBinding
import com.kdjj.presentation.databinding.ItemEditorRecipeMetaBinding
import com.kdjj.presentation.databinding.ItemEditorRecipeStepBinding
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.viewmodel.recipeeditor.RecipeEditorViewModel

internal class RecipeEditorListAdapter(private val viewModel: RecipeEditorViewModel) :
    ListAdapter<RecipeEditorItem, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<RecipeEditorItem>() {

            override fun areItemsTheSame(
                oldItem: RecipeEditorItem,
                newItem: RecipeEditorItem
            ): Boolean = when {
                oldItem is RecipeEditorItem.RecipeMetaModel &&
                        newItem is RecipeEditorItem.RecipeMetaModel ->
                    oldItem.recipeId == newItem.recipeId
                oldItem is RecipeEditorItem.RecipeStepModel &&
                        newItem is RecipeEditorItem.RecipeStepModel ->
                    oldItem.hashCode() == newItem.hashCode()
                oldItem is RecipeEditorItem.PlusButton && newItem is RecipeEditorItem.PlusButton -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: RecipeEditorItem,
                newItem: RecipeEditorItem
            ): Boolean = oldItem == newItem
        }
    ) {

    class RecipeMetaViewHolder(
        private val binding: ItemEditorRecipeMetaBinding, viewModel: RecipeEditorViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageViewEditorRecipe.clipToOutline = true
            binding.lifecycleOwner?.let { owner ->
                viewModel.liveRecipeTypes.observe(owner) { types ->
                    binding.spinnerEditorRecipeType.adapter = ArrayAdapter(
                        binding.root.context,
                        R.layout.item_editor_recipe_type,
                        types.map { it.title }
                    )
                }
            }
            binding.editorViewModel = viewModel
        }

        fun bind(item: RecipeEditorItem.RecipeMetaModel) {
            binding.model = item
            binding.executePendingBindings()
        }

        fun onViewRecycled() {
            Glide.with(binding.root.context).clear(binding.imageViewEditorRecipe)
        }
    }

    class RecipeStepViewHolder(
        private val binding: ItemEditorRecipeStepBinding,
        private val viewModel: RecipeEditorViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageViewEditorStep.clipToOutline = true
            binding.spinnerEditorStepType.adapter = ArrayAdapter(
                binding.root.context,
                R.layout.item_editor_recipe_type,
                viewModel.stepTypes.map { it.name }
            )
            binding.editorViewModel = viewModel
        }

        fun bind(item: RecipeEditorItem.RecipeStepModel) {
            binding.model = item
            binding.executePendingBindings()
        }

        fun onViewRecycled() {
            Glide.with(binding.root.context).clear(binding.imageViewEditorStep)
        }
    }

    class AddStepViewHolder(
        private val binding: ItemEditorAddStepBinding,
        private val viewModel: RecipeEditorViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = viewModel
        }

        fun bind() {
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is RecipeEditorItem.RecipeMetaModel -> TYPE_META
            is RecipeEditorItem.RecipeStepModel -> TYPE_STEP
            RecipeEditorItem.PlusButton -> TYPE_ADD
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
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
                RecipeMetaViewHolder(binding, viewModel)
            }
            TYPE_STEP -> {
                val binding = ItemEditorRecipeStepBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
                RecipeStepViewHolder(binding, viewModel)
            }
            else -> {
                val binding = ItemEditorAddStepBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
                AddStepViewHolder(binding, viewModel)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (item as? RecipeEditorItem.RecipeMetaModel)?.let { (holder as RecipeMetaViewHolder).bind(item) }
            ?: (item as? RecipeEditorItem.RecipeStepModel)?.let { (holder as RecipeStepViewHolder).bind(item) }
            ?: (item as? RecipeEditorItem.PlusButton)?.let { (holder as AddStepViewHolder).bind() }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is RecipeStepViewHolder -> holder.onViewRecycled()
            is RecipeMetaViewHolder -> holder.onViewRecycled()
        }
    }

    companion object {
        private const val TYPE_META = 0
        const val TYPE_STEP = 1
        private const val TYPE_ADD = 2
    }
}