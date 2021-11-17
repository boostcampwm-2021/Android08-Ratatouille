package com.kdjj.presentation.view.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
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

    override fun bind(
        holder: SingleViewTypeViewHolder<StepTimerModel, ItemDetailTimerBinding>,
        item: StepTimerModel
    ) {
        with(holder.binding) {
            model = item
            lifecycleOwner?.let {
                item.eventAnimation.observe(it, EventObserver {
                    cardViewTimer.setCardBackgroundColor(root.context.getColor(R.color.red_500))
                    startTimerFinishAnimation(root)
                })
            }
        }
    }

    private fun startTimerFinishAnimation(view: View) {
        val translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, -20f, 0f, 20f, 0f)
        ObjectAnimator.ofPropertyValuesHolder(
            view,
            translateX
        ).apply {
            duration = 100
            repeatCount = 20
        }.start()
    }
}