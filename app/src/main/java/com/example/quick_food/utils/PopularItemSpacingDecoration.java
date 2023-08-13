package com.example.quick_food.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PopularItemSpacingDecoration extends RecyclerView.ItemDecoration {
    private final int spacingBetween;
    private final int spacingStartEnd;

    public PopularItemSpacingDecoration(int spacingBetween, int spacingStartEnd) {
        this.spacingBetween = spacingBetween;
        this.spacingStartEnd = spacingStartEnd;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = spacingStartEnd;
            outRect.right = spacingBetween;
        }
        else if (parent.getChildLayoutPosition(view) > 0 && parent.getChildLayoutPosition(view) < parent.getAdapter().getItemCount() - 1) {
            outRect.right = spacingBetween;
        }
        else if (parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.right = spacingStartEnd;
        }
    }
}
