package com.kdjj.presentation.view.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kdjj.domain.model.Recipe

@BindingAdapter("calculateTotalTime")
fun TextView.calculateTotalTime(recipe: Recipe) {
    val secs = recipe.stepList.map { it.seconds }.reduce { acc, i -> acc + i }
    val min = secs / 60
    this.text = if (min == 0) secs.toString() + "초"
                else "$min 분 ${secs%60} 초"
}

@BindingAdapter("calculateUpdateTime")
fun TextView.calculateUpdateTime(recipe: Recipe) {
    val secs = (System.currentTimeMillis() - recipe.createTime) / 1000

    // 분 60     시간 3600    하루 24 * 3600    1달 : 30 * 24 * 3600     1년 : 365 * 24 * 3600
    val year = secs / 31536000
    val month = secs / 2592000
    val day = secs / 86400
    val hour = secs / 3600
    val min = secs / 60

    this.text = if (year != 0L) year.toString() + "년 전"
                else if (month != 0L) month.toString() + "달 전"
                else if (day != 0L) day.toString() + "일 전"
                else if (hour != 0L) hour.toString() + "시간 전"
                else if (min != 0L) min.toString() + "분 전"
                else secs.toString() + "초 전"
}