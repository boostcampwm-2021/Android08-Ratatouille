package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kdjj.presentation.databinding.ItemListRecipeBinding
import com.kdjj.presentation.model.RecipeListItemModel
import com.kdjj.presentation.viewmodel.search.SearchViewModel

class SearchRecipeListAdapter(
    private val viewModel: SearchViewModel,
) : SingleViewTypeListAdapter<RecipeListItemModel, ItemListRecipeBinding>(RecipeListItemModelDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemListRecipeBinding =
        ItemListRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun initViewHolder(
        binding: ItemListRecipeBinding,
        getItemPosition: () -> Int,
    ) {
        binding.root.setOnClickListener {
            viewModel.moveToSummary(getItem(getItemPosition()))
        }
    }

    override fun bind(
        holder: SingleViewTypeViewHolder<RecipeListItemModel, ItemListRecipeBinding>,
        item: RecipeListItemModel,
    ) {
       with(holder.binding) {
           recipe = item
           executePendingBindings()
       }
    }
}