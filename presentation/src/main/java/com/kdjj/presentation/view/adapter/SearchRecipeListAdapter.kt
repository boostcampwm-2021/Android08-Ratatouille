package com.kdjj.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.kdjj.presentation.databinding.ItemOthersRecipeBinding
import com.kdjj.presentation.model.OthersRecipeModel
import com.kdjj.presentation.viewmodel.others.OthersViewModel
import com.kdjj.presentation.viewmodel.search.SearchViewModel

class SearchRecipeListAdapter(
    private val viewModel: SearchViewModel,
) : SingleViewTypeListAdapter<OthersRecipeModel, ItemOthersRecipeBinding>(OthersRecipeModelDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemOthersRecipeBinding =
        ItemOthersRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun initViewHolder(
        binding: ItemOthersRecipeBinding,
        getItemPosition: () -> Int,
    ) {
        binding.root.setOnClickListener {
            viewModel.moveToSummary(getItem(getItemPosition()))
        }
    }

    override fun bind(
        holder: SingleViewTypeViewHolder<OthersRecipeModel, ItemOthersRecipeBinding>,
        item: OthersRecipeModel,
    ) {
       with(holder.binding) {
           recipe = item
           executePendingBindings()
       }
    }
}