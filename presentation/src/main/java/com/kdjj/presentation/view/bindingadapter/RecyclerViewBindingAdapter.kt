package com.kdjj.presentation.view.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
@BindingAdapter("app:submitList")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.submitList(list: List<T>?) {
    (adapter as? ListAdapter<T, VH>)?.submitList(list)
}

@BindingAdapter("app:moveTo")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.moveTo(i: Int?) {
    smoothScrollToPosition(i ?: return)
}