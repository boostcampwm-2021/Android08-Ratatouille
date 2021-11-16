package com.kdjj.presentation.common

object DisplayConverter {
    private var density = 0f

    fun dpToPx(dp: Int) = (dp * density)
    fun pxToDp(px: Int) = (px / density + 0.5).toInt()

    fun setDensity(density: Float) {
        this.density = density
    }
}