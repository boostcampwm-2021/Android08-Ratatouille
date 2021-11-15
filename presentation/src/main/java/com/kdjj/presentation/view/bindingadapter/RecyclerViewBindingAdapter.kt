package com.kdjj.presentation.view.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
@BindingAdapter("app:submitList")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.submitList(list: List<T>?) {
    (adapter as? ListAdapter<T, VH>)?.submitList(list)
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter(value = ["list", "callback"], requireAll = true)
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.submitList(list: List<T>?, callback: Runnable) {
    (adapter as? ListAdapter<T, VH>)?.submitList(list, callback)
}


@BindingAdapter("app:moveTo")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.moveTo(i: Int?) {
    smoothScrollToPosition(i ?: return)
}