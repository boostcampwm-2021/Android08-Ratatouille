package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ItemMyrecipeAddRecipeBinding
import com.kdjj.presentation.databinding.ItemMyrecipeRecipeBinding
import com.kdjj.presentation.model.MyRecipeItem

class MyRecipeListAdapter(private val navigation: NavController) :
    ListAdapter<MyRecipeItem, RecyclerView.ViewHolder>(MyRecipeItemCallback()) {

    inner class MyRecipeViewHolder(private val binding: ItemMyrecipeRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyRecipeItem.MyRecipe) {
            binding.textviewMyRecipeTitle.text = item.recipe.title
        }
    }

    inner class AddRecipeViewHolder(private val binding: ItemMyrecipeAddRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is MyRecipeItem.MyRecipe -> TYPE_RECIPE
            else -> TYPE_ADD
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_RECIPE -> {
                val binding = ItemMyrecipeRecipeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MyRecipeViewHolder(binding)
            }
            else -> {
                val binding = ItemMyrecipeAddRecipeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddRecipeViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is MyRecipeItem.MyRecipe -> (holder as MyRecipeViewHolder).bind(item)
        }
    }

    companion object {
        private const val TYPE_RECIPE = 0
        private const val TYPE_ADD = 1
    }
}