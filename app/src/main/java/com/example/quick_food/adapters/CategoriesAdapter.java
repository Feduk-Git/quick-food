package com.example.quick_food.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.R;
import com.example.quick_food.ServerConnection;
import com.example.quick_food.interfaces.OnCategoryDetailsClickListener;
import com.example.quick_food.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>{
    private final List<Category> items;
    private final OnCategoryDetailsClickListener categoryDetailsClickListener;

    public CategoriesAdapter(List<Category> items, OnCategoryDetailsClickListener categoryDetailsClickListener) {
        this.items = items;
        this.categoryDetailsClickListener = categoryDetailsClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_category, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = items.get(position);
        holder.categoryNameTV.setText(category.name);

        Picasso.get().load(ServerConnection.getInstance().host + "api/image/get_category_image/" + category.id).into(holder.categoryImage);

        holder.categoryDetailsBT.setOnClickListener(v -> {
            categoryDetailsClickListener.onCategoryDetailsClicked(items.get(position).name);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameTV;
        public ImageView categoryImage;
        public Button categoryDetailsBT;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTV = itemView.findViewById(R.id.category_name__popular_category);
            categoryImage = itemView.findViewById(R.id.category_img__popular_category);
            categoryDetailsBT = itemView.findViewById(R.id.category_details_bt__popular_category);
        }
    }
}
