package com.kdjj.presentation.view.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.databinding.ItemMyRecipeAddRecipeBinding
import com.kdjj.presentation.databinding.ItemMyRecipeBinding
import com.kdjj.presentation.databinding.ItemMyRecipeProgressBinding
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.viewmodel.my.MyRecipeViewModel

internal class MyRecipeListAdapter(private val viewModel: MyRecipeViewModel) :
    ListAdapter<MyRecipeItem, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<MyRecipeItem>() {

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
                    oldItem.recipe.recipeId == newItem.recipe.recipeId
                }
                oldItem is MyRecipeItem.PlusButton && newItem is MyRecipeItem.PlusButton -> true
                else -> false
            }
        }
    }) {

    inner class MyRecipeViewHolder(private val binding: ItemMyRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.lifecycleOwner?.let { owner ->
                viewModel.liveRecipeItemSelected.observe(owner) { myRecipe ->
                    myRecipe?.let {
                        if (absoluteAdapterPosition != -1 && myRecipe == getItem(
                                absoluteAdapterPosition
                            )
                        ) {
                            startAnimation(binding.textViewMyRecipeTitle)
                            startAnimation(binding.textViewTimeTitle)
                            startAnimation(binding.textViewMyRecipeTime)
                            startAnimation(binding.textViewMyRecipeDescriptionTitle)
                            startAnimation(binding.textViewMyRecipeDescription)
                        }
                    }
                }
            }
        }

        fun bind(item: MyRecipeItem.MyRecipe) {
            binding.myRecipeViewModel = viewModel
            binding.myRecipeItem = item
        }
    }

    inner class AddRecipeViewHolder(private val binding: ItemMyRecipeAddRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.myRecipeViewModel = viewModel
        }
    }

    inner class ProgressViewHolder(private val binding: ItemMyRecipeProgressBinding) :
        RecyclerView.ViewHolder(binding.root) { }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is MyRecipeItem.MyRecipe -> TYPE_RECIPE
            is MyRecipeItem.PlusButton -> TYPE_ADD
            else -> TYPE_PROGRESS
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_RECIPE -> {
                val binding = ItemMyRecipeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
                MyRecipeViewHolder(binding)
            }
            TYPE_ADD -> {
                val binding = ItemMyRecipeAddRecipeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
                AddRecipeViewHolder(binding)
            }
            else -> {
                val binding = ItemMyRecipeProgressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
                ProgressViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is MyRecipeItem.MyRecipe -> (holder as MyRecipeViewHolder).bind(item)
        }
    }

    private fun startAnimation(view: View) {
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 100f, 0f).apply {
            duration = 200
            start()
        }
    }

    companion object {
        private const val TYPE_RECIPE = 0
        private const val TYPE_ADD = 1
        private const val TYPE_PROGRESS = 2
    }
}