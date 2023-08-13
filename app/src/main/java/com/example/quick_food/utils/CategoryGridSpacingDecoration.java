package com.example.quick_food.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryGridSpacingDecoration extends RecyclerView.ItemDecoration {
    private final int spacingBetween;
    private final int spacingStartEnd;

    public CategoryGridSpacingDecoration(int spacingBetween, int spacingStartEnd) {
        this.spacingBetween = spacingBetween;
        this.spacingStartEnd = spacingStartEnd;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();
        int row = spanIndex % 2; // item column

        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            outRect.left = spacingStartEnd;
        }

        outRect.right = spacingBetween;
        if (row == 0)
            outRect.bottom = spacingBetween;

        if (parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.right = spacingStartEnd;
        }
    }
}