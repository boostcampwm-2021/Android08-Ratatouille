package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ItemListRecipeBinding
import com.kdjj.presentation.model.RecipeListItemModel
import com.kdjj.presentation.viewmodel.others.OthersViewModel

class OthersRecipeListAdapter(
    private val viewModel: OthersViewModel,
) : SingleViewTypeListAdapter<RecipeListItemModel, ItemListRecipeBinding>(RecipeListItemModelDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemListRecipeBinding =
        ItemListRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun initViewHolder(
        binding: ItemListRecipeBinding,
        getItemPosition: () -> Int,
    ) {
        binding.root.setOnClickListener {
            viewModel.recipeItemClick(getItem(getItemPosition()))
        }
    }

    override fun bind(
        holder: SingleViewTypeViewHolder<ItemListRecipeBinding>,
        item: RecipeListItemModel,
    ) {
       with(holder.binding) {
           recipe = item
           executePendingBindings()
       }
    }

    override fun onViewRecycled(holder: SingleViewTypeViewHolder<ItemListRecipeBinding>) {
        with(holder.binding) {
            Glide.with(root.context).clear(imageViewOthersItemImg)
        }
    }
}

class RecipeListItemModelDiffCallback : DiffUtil.ItemCallback<RecipeListItemModel>() {

    override fun areItemsTheSame(oldItem: RecipeListItemModel, newItem: RecipeListItemModel): Boolean =
        oldItem.recipeId == newItem.recipeId

    override fun areContentsTheSame(oldItem: RecipeListItemModel, newItem: RecipeListItemModel): Boolean =
        true
}
