package com.example.quick_food.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.DishModel;
import com.example.quick_food.R;

import java.util.List;

public class PopularItemsAdapter extends RecyclerView.Adapter<PopularItemsAdapter.PopularItemViewHolder>{
    private final List<DishModel> items;
    private OnProductDetailsClickListener productDetailsClickListener;

    public PopularItemsAdapter(List<DishModel> items, OnProductDetailsClickListener productDetailsClickListener) {
        this.items = items;
        this.productDetailsClickListener = productDetailsClickListener;
    }

    @NonNull
    @Override
    public PopularItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull PopularItemViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.img2);
        holder.textView.setText(items.get(position).name);

        holder.rootCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(items.get(position)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PopularItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout rootCL;

        public PopularItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image__popular_item);
            textView = itemView.findViewById(R.id.item_name__popular_item);
            rootCL = itemView.findViewById(R.id.root_cl__popular_item);
        }
    }
}
