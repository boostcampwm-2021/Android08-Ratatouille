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

    inner class MyRecipeViewHolder(val binding: ItemMyRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyRecipeItem.MyRecipe) {
            binding.myRecipeViewModel = viewModel
            binding.myRecipeItem = item

            binding.lifecycleOwner?.let { owner ->
                viewModel.liveRecipeItemSelected.observe(owner) { myRecipe ->
                    if(myRecipe == item){
                        setAnimate(binding.textViewMyRecipeTitle)
                        setAnimate(binding.textViewMyRecipeTime)
                        setAnimate(binding.textViewMyRecipeDescriptionTitle)
                        setAnimate(binding.textViewMyRecipeDescription)
                    }
                }
            }
        }
    }

    private fun setAnimate(view: View){
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 100f, 0f).apply {
            duration = 200
            start()
        }
    }

    inner class AddRecipeViewHolder(val binding: ItemMyRecipeAddRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.myRecipeViewModel = viewModel
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is MyRecipeItem.MyRecipe -> TYPE_RECIPE
            else -> TYPE_ADD
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
            else -> {
                val binding = ItemMyRecipeAddRecipeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
                AddRecipeViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is MyRecipeItem.MyRecipe -> (holder as MyRecipeViewHolder).bind(item)
        }
    }

    companion object {
        private const val TYPE_RECIPE = 0
        private const val TYPE_ADD = 1
    }
}