package com.kdjj.presentation.view.bindingadapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleProgressBar")
fun ProgressBar.setVisibleProgressBar(state: Boolean) {
    if (state) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}