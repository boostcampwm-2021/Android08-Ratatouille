package com.kdjj.presentation.view.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kdjj.domain.model.Recipe
import com.kdjj.presentation.R
import com.kdjj.presentation.common.calculateTotalTime

@BindingAdapter("calculateTotalTime")
fun TextView.setTotalTimeText(recipe: Recipe?) {
    val totalTime = calculateTotalTime(recipe ?: return)
    this.text = "소요시간: $totalTime"
}

@BindingAdapter("formatTotalTime")
fun TextView.formatTotalTime(totalTime: Int) {
    val min = totalTime / 60
    this.text = if (min == 0) context.getString(R.string.total_time_seconds, totalTime.toString())
    else context.getString(R.string.total_time_min_seconds, min.toString(), (totalTime%60).toString())
}

@BindingAdapter("calculateUpdateTime")
fun TextView.calculateUpdateTime(createTime: Long) {
    val secs = (System.currentTimeMillis() - createTime) / 1000

    // 분 60     시간 3600    하루 24 * 3600    1달 : 30 * 24 * 3600     1년 : 365 * 24 * 3600
    val year = secs / 31536000
    val month = secs / 2592000
    val day = secs / 86400
    val hour = secs / 3600
    val min = secs / 60

    this.text = if (year != 0L) context.getString(R.string.update_time_year, year.toString())
    else if (month != 0L) context.getString(R.string.update_time_month, month.toString())
    else if (day != 0L) context.getString(R.string.update_time_day, day.toString())
    else if (hour != 0L) context.getString(R.string.update_time_hours, hour.toString())
    else if (min != 0L) context.getString(R.string.update_time_minutes, min.toString())
    else context.getString(R.string.update_time_seconds, secs.toString())
}