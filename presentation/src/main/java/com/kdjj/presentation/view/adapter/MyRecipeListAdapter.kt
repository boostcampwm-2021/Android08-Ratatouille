package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.databinding.ItemMyRecipeAddRecipeBinding
import com.kdjj.presentation.databinding.ItemMyRecipeBinding
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.viewmodel.my.MyRecipeViewModel

internal class MyRecipeListAdapter(private val viewModel: MyRecipeViewModel) :
    ListAdapter<MyRecipeItem, RecyclerView.ViewHolder>(MyRecipeItemCallback()) {

    inner class MyRecipeViewHolder(val binding: ItemMyRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyRecipeItem.MyRecipe) {
            binding.myRecipeViewModel = viewModel
            binding.myRecipeItem = item
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

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is MyRecipeViewHolder -> {
                holder.binding.lifecycleOwner = holder.itemView.findViewTreeLifecycleOwner()
            }
            is AddRecipeViewHolder -> {
                holder.binding.lifecycleOwner = holder.itemView.findViewTreeLifecycleOwner()
            }
        }
    }

    companion object {
        private const val TYPE_RECIPE = 0
        private const val TYPE_ADD = 1
    }
}