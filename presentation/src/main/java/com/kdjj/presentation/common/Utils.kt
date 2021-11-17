package com.kdjj.presentation.common

import com.kdjj.domain.model.Recipe

internal fun calculateSeconds(min: Int, sec: Int): Int {
    return min * 60 + sec
}

internal fun calculateTotalTime(recipe: Recipe?): Int {
    return recipe?.let { it.stepList.map { it.seconds }.reduce { acc, i -> acc + i } } ?: 0
}
