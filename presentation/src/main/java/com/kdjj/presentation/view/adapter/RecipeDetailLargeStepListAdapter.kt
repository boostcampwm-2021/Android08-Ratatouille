package com.kdjj.presentation.view.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.databinding.ItemDetailLargeStepBinding
import com.kdjj.presentation.databinding.ItemDetailStepBinding
import com.kdjj.presentation.viewmodel.recipedetail.RecipeDetailViewModel

class RecipeDetailLargeStepListAdapter(
    private val viewModel: RecipeDetailViewModel
) : SingleViewTypeListAdapter<RecipeStep, ItemDetailLargeStepBinding>(
    object : DiffUtil.ItemCallback<RecipeStep>() {

        override fun areItemsTheSame(oldItem: RecipeStep, newItem: RecipeStep): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: RecipeStep, newItem: RecipeStep): Boolean =
            oldItem.name == newItem.name &&
                    oldItem.type == newItem.type &&
                    oldItem.description == newItem.description &&
                    oldItem.seconds == newItem.seconds &&
                    oldItem.imgPath == newItem.imgPath
    }
) {

    override fun createBinding(parent: ViewGroup): ItemDetailLargeStepBinding =
        ItemDetailLargeStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = parent.findViewTreeLifecycleOwner()
            }

    override fun initViewHolder(binding: ItemDetailLargeStepBinding, getItemPosition: () -> Int) {

    }

    override fun bind(
        holder: SingleViewTypeViewHolder<ItemDetailLargeStepBinding>,
        item: RecipeStep
    ) {
        holder.binding.step = item
        holder.binding.executePendingBindings()
    }
}