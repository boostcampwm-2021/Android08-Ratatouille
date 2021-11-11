package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.kdjj.domain.model.Recipe
import com.kdjj.presentation.databinding.ItemOthersRecipeBinding

class OthersRecipeListAdapter(
) : BaseListAdapter<Recipe, ItemOthersRecipeBinding>(RecipeDiffCallback()) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemOthersRecipeBinding =
        ItemOthersRecipeBinding.inflate(LayoutInflater.from(parent.context))

    override fun initViewHolder(
        binding: ItemOthersRecipeBinding,
        getItemPosition: () -> Int,
        viewType: Int
    ) {
        binding.root.setOnClickListener {
            //todo: clicklisntener
        }
    }

    override fun bind(
        holder: BaseViewHolder<ItemOthersRecipeBinding>,
        item: Recipe,
        viewType: Int
    ) {
       with(holder.binding) {
            //todo : binding
       }
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
        oldItem.recipeId == newItem.recipeId

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
        true
}
