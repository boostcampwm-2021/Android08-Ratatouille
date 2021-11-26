package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.databinding.ItemDetailLargeStepBinding
import com.kdjj.presentation.model.StepTimerModel
import com.kdjj.presentation.viewmodel.recipedetail.RecipeDetailViewModel

class RecipeDetailLargeStepListAdapter(
    private val viewModel: RecipeDetailViewModel
) : SingleViewTypeListAdapter<StepTimerModel, ItemDetailLargeStepBinding>(
    object : DiffUtil.ItemCallback<StepTimerModel>() {

        override fun areItemsTheSame(oldItem: StepTimerModel, newItem: StepTimerModel): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: StepTimerModel, newItem: StepTimerModel): Boolean =
            oldItem.recipeStep.name == newItem.recipeStep.name &&
                    oldItem.recipeStep.type == newItem.recipeStep.type &&
                    oldItem.recipeStep.description == newItem.recipeStep.description &&
                    oldItem.recipeStep.seconds == newItem.recipeStep.seconds &&
                    oldItem.recipeStep.imgPath == newItem.recipeStep.imgPath
    }
) {

    override fun createBinding(parent: ViewGroup): ItemDetailLargeStepBinding =
        ItemDetailLargeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = parent.findViewTreeLifecycleOwner()
            }

    override fun initViewHolder(binding: ItemDetailLargeStepBinding, getItemPosition: () -> Int) {
        binding.viewModel = viewModel
    }

    override fun bind(
        holder: SingleViewTypeViewHolder<ItemDetailLargeStepBinding>,
        item: StepTimerModel
    ) {
        holder.binding.model = item
        holder.binding.executePendingBindings()
    }
}