package com.kdjj.presentation.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdjj.presentation.R

@Suppress("UNCHECKED_CAST")
@BindingAdapter("app:submitList")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.submitList(list: List<T>?) {
    (adapter as? ListAdapter<T, VH>)?.submitList(list)
}

@BindingAdapter("app:loadImage", "app:defaultImage", requireAll = false)
fun ImageView.loadImage(src: String?, defaultResId: Int?) {
    Glide.with(context)
        .load(if (src.isNullOrBlank()) defaultResId ?: R.drawable.ic_my_recipe_24dp else src)
        .into(this)
}