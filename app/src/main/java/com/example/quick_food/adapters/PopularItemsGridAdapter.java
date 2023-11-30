package com.example.quick_food.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.R;
import com.example.quick_food.activities.MainActivity;
import com.example.quick_food.fragments.DishDetailsFragment;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.DishModel;

import java.util.List;

public class PopularItemsGridAdapter extends RecyclerView.Adapter<PopularItemsGridAdapter.PopularItemGridViewHolder> {
    private final List<DishModel> items;
    private final Context context;
    private final OnProductDetailsClickListener productDetailsClickListener;

    public PopularItemsGridAdapter(List<DishModel> items, Context context, OnProductDetailsClickListener productDetailsClickListener) {
        this.items = items;
        this.context = context;
        this.productDetailsClickListener = productDetailsClickListener;
    }

    @NonNull
    @Override
    public PopularItemGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularItemGridViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item_grid, parent, false)) {
        };
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull PopularItemGridViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemImage.getLayoutParams();
        int screenWidth = getScreenWidth();
        layoutParams.width = (screenWidth * 27) / 100;
        layoutParams.height = layoutParams.width;
        holder.itemImage.setLayoutParams(layoutParams);
        holder.itemNameTV.setText(items.get(position).name);

        holder.rootCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(items.get(position)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PopularItemGridViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemNameTV;
        public ConstraintLayout rootCL;

        public PopularItemGridViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image__popular_item);
            itemNameTV = itemView.findViewById(R.id.item_name__popular_item);
            rootCL = itemView.findViewById(R.id.root_cl__popular_item_grid);
        }
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
        return items.size();
    }
}
