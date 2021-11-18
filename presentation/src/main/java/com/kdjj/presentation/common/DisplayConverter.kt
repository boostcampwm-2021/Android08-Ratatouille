package com.kdjj.presentation.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DisplayConverter @Inject constructor(
    @ApplicationContext context: Context
) {
    private var density = context.resources.displayMetrics.density

    fun dpToPx(dp: Int) = (dp * density)

    fun pxToDp(px: Int) = (px / density + 0.5).toInt()
}