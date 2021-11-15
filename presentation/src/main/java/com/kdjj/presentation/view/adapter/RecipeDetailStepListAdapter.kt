package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.databinding.ItemDetailStepBinding
import com.kdjj.presentation.viewmodel.recipedetail.RecipeDetailViewModel

class RecipeDetailStepListAdapter(
    private val viewModel: RecipeDetailViewModel
) : SingleViewTypeListAdapter<RecipeStep, ItemDetailStepBinding>(
    object : DiffUtil.ItemCallback<RecipeStep>() {

        override fun areItemsTheSame(oldItem: RecipeStep, newItem: RecipeStep): Boolean =
            oldItem.stepId == newItem.stepId

        override fun areContentsTheSame(oldItem: RecipeStep, newItem: RecipeStep): Boolean =
            oldItem.name == newItem.name &&
                    oldItem.description == newItem.description &&
                    oldItem.seconds == newItem.seconds &&
                    oldItem.imgPath == newItem.imgPath
    }
) {

    override fun createBinding(parent: ViewGroup): ItemDetailStepBinding =
        ItemDetailStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = parent.findViewTreeLifecycleOwner()
            }

    override fun initViewHolder(binding: ItemDetailStepBinding, getItemPosition: () -> Int) {

    }

    override fun bind(
        holder: SingleViewTypeViewHolder<RecipeStep, ItemDetailStepBinding>,
        item: RecipeStep
    ) {
        holder.binding.step = item
    }
}