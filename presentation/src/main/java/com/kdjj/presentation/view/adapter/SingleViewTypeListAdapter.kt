package com.kdjj.presentation.view.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class SingleViewTypeListAdapter<T, VDB : ViewDataBinding> constructor(
    diffUtil : DiffUtil.ItemCallback<T>,
) : ListAdapter<T, SingleViewTypeViewHolder<VDB>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewTypeViewHolder<VDB> {
        val binding = createBinding(parent)
        return SingleViewTypeViewHolder(binding, { b, getItemPosition -> initViewHolder(b, getItemPosition)} )
    }

    protected abstract fun createBinding(parent: ViewGroup): VDB

    protected open fun initViewHolder(binding: VDB, getItemPosition: () -> Int) {}

    override fun onBindViewHolder(holder: SingleViewTypeViewHolder<VDB>, position: Int) {
        bind(holder, getItem(position))
    }

    protected abstract fun bind(holder: SingleViewTypeViewHolder<VDB>, item: T)
}

class SingleViewTypeViewHolder<VDB : ViewDataBinding> constructor(
    val binding: VDB,
    onViewHolderInit: (VDB, getAdapterPosition: () -> Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        onViewHolderInit.invoke(binding, { bindingAdapterPosition })
    }
}