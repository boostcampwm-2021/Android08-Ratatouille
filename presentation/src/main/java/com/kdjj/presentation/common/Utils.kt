package com.kdjj.presentation.common

import com.kdjj.domain.model.Recipe


internal fun calculateSeconds(min: Int, sec: Int): Int{
    return min*60 + sec
}

internal fun calculateTotalTime(recipe: Recipe): String {
    val secs = recipe.stepList.map { it.seconds }.reduce { acc, i -> acc + i }
    val min = secs / 60
    return if (min == 0) secs.toString() + "초"
    else "$min 분 ${secs%60} 초"
}