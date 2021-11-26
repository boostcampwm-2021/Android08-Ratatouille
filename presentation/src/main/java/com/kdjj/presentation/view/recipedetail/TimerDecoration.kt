package com.kdjj.presentation.view.recipedetail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.R

class TimerDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.left = view.resources.getDimensionPixelSize(R.dimen.detailItem_marginHorizontal) -
                        view.resources.getDimensionPixelSize(R.dimen.detail_timer_marginHorizontal)
            }
            (parent.adapter?.itemCount ?: 0) - 1 -> {
                outRect.right = view.resources.getDimensionPixelSize(R.dimen.detailItem_marginHorizontal) -
                        view.resources.getDimensionPixelSize(R.dimen.detail_timer_marginHorizontal)
            }
        }
    }
}