package com.kdjj.presentation.view.adapter

import androidx.databinding.InverseMethod

internal object BindingConverter {

    @InverseMethod("stringToInt")
    @JvmStatic fun intToString(value: Int?) = value?.toString() ?: ""

    @JvmStatic fun stringToInt(value: String) = if (value.isNotEmpty()) value.toInt() else null
}