package com.kdjj.presentation.view.home.my

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.kdjj.presentation.R

class TextRadioButton : AppCompatRadioButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        setTypeface(null, if (checked) Typeface.BOLD else Typeface.NORMAL)
        if(checked) setTextColor(context.getColor(R.color.blue_600)) else setTextColor(context.getColor(R.color.dark_500))
    }
}