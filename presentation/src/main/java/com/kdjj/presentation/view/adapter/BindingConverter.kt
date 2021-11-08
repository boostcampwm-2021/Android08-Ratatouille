package com.kdjj.presentation.view.adapter

import android.widget.EditText
import androidx.databinding.InverseMethod

object BindingConverter {

    @InverseMethod("stringToInt")
    @JvmStatic fun intToString(value: Int) = value.toString()

    @JvmStatic fun stringToInt(value: String) = value.toInt()
}