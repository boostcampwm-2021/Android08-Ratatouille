package com.kdjj.presentation.view.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, VDB : ViewDataBinding> constructor(
    diffUtil : DiffUtil.ItemCallback<T>,
) : ListAdapter<T, BaseViewHolder<VDB>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VDB> {
        val binding = createBinding(parent, viewType)
        return BaseViewHolder(binding, { b, getItemPosition, itemViewType -> initViewHolder(b, getItemPosition, itemViewType)} )
    }

    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): VDB

    protected open fun initViewHolder(binding: VDB, getItemPosition: () -> Int, viewType: Int) {}

    override fun onBindViewHolder(holder: BaseViewHolder<VDB>, position: Int) {
        bind(holder, getItem(position), holder.itemViewType)
    }

    protected abstract fun bind(holder: BaseViewHolder<VDB>, item: T, viewType: Int)
}

class BaseViewHolder<VDB : ViewDataBinding> constructor(
    val binding: VDB,
    onViewHolderInit: (VDB, getAdapterPosition: () -> Int, viewType: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        onViewHolderInit.invoke(binding, { bindingAdapterPosition }, itemViewType)
    }
}