package com.kdjj.presentation.view.bindingadapter

import androidx.databinding.InverseMethod

internal object BindingConverter {

    @InverseMethod("stringToMinutes")
    @JvmStatic fun minutesToString(value: Int?) = value?.toString() ?: ""

    @JvmStatic fun stringToMinutes(value: String) = if (value.isNotEmpty()) minOf(value.toInt(), 999) else null

    @InverseMethod("stringToSeconds")
    @JvmStatic fun secondsToString(value: Int?) = value?.toString() ?: ""

    @JvmStatic fun stringToSeconds(value: String) = if (value.isNotEmpty()) minOf(value.toInt(), 59) else null
}