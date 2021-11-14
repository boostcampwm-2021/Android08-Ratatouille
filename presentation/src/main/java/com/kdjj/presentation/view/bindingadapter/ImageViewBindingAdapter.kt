package com.kdjj.presentation.view.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdjj.presentation.R

@BindingAdapter("app:loadImage", "app:defaultImage", requireAll = false)
fun ImageView.loadImage(src: String?, defaultResId: Int?) {
    Glide.with(context)
        .load(if (src.isNullOrBlank()) defaultResId ?: R.drawable.ic_my_recipe_24dp else src)
        .into(this)
}