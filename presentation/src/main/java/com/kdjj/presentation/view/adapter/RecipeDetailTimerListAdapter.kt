package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.kdjj.presentation.databinding.ItemDetailTimerBinding
import com.kdjj.presentation.model.StepTimerModel
import com.kdjj.presentation.viewmodel.recipedetail.RecipeDetailViewModel

class RecipeDetailTimerListAdapter(
    private val viewModel: RecipeDetailViewModel
) : SingleViewTypeListAdapter<StepTimerModel, ItemDetailTimerBinding>(
    object : DiffUtil.ItemCallback<StepTimerModel>() {

        override fun areItemsTheSame(oldItem: StepTimerModel, newItem: StepTimerModel): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: StepTimerModel, newItem: StepTimerModel): Boolean =
            oldItem.recipeStep.name == newItem.recipeStep.name
    }
) {
    override fun createBinding(parent: ViewGroup): ItemDetailTimerBinding =
        ItemDetailTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = parent.findViewTreeLifecycleOwner()
            }

    override fun initViewHolder(binding: ItemDetailTimerBinding, getItemPosition: () -> Int) {

    }

    override fun bind(
        holder: SingleViewTypeViewHolder<StepTimerModel, ItemDetailTimerBinding>,
        item: StepTimerModel
    ) {
        holder.binding.model = item
    }
}