package com.kdjj.presentation.view.bindingadapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kdjj.domain.model.Recipe
import com.kdjj.presentation.common.calculateTotalTime

@BindingAdapter("calculateTotalTime")
fun TextView.setTotalTimeText(recipe: Recipe?) {

    val totalTime = calculateTotalTime(recipe?: return)
    this.text = "소요시간: $totalTime"
}
