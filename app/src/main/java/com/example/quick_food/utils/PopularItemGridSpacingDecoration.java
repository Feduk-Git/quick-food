package com.example.quick_food.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PopularItemGridSpacingDecoration extends RecyclerView.ItemDecoration{
    private final int spacingBetween;
    private final int spacingBottom;

    public PopularItemGridSpacingDecoration(int spacingBetween, int spacingBottom) {
        this.spacingBetween = spacingBetween;
        this.spacingBottom = spacingBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();
        int column = spanIndex % 3; // item column

        if (column == 0)
            outRect.bottom = spacingBetween;

        if (parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = spacingBottom;
        }
    }
}
