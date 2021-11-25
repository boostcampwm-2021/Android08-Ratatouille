package com.kdjj.presentation.view.recipedetail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StepItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
            outRect.bottom = parent.height - view.height
        }
    }
}