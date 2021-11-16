package com.kdjj.presentation.view.home.my

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(
    private val spanCount: Int,
    private val totalWidth: Int,
    private val itemWidth: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val space = (totalWidth - itemWidth * spanCount) / (spanCount + 1)

        when (position % spanCount) {
            0 -> {
                outRect.left = space
                outRect.right = space / 2
            }
            spanCount - 1 -> {
                outRect.left = space / 2
                outRect.right = space
            }
            else -> {
                outRect.left = space / 2
                outRect.right = space / 2
            }
        }
    }
}