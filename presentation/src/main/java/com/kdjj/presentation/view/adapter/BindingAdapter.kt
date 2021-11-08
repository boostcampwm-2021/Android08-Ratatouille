package com.kdjj.presentation.view.adapter

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.R

@Suppress("UNCHECKED_CAST")
@BindingAdapter("app:submitList")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.submitList(list: List<T>?) {
    (adapter as? ListAdapter<T, VH>)?.let { it.submitList(list) }
}

@BindingAdapter("app:typeSpinnerItems")
fun Spinner.typeSpinnerItems(items: List<String>) {
    val spinnerAdapter = ArrayAdapter(
        context,
        R.layout.item_editor_recipe_type,
        items
    )
    adapter = spinnerAdapter
}