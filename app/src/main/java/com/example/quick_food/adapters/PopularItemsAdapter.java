package com.example.quick_food.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.ServerConnection;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.Product;
import com.example.quick_food.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularItemsAdapter extends RecyclerView.Adapter<PopularItemsAdapter.PopularItemViewHolder>{
    private final List<Product> items;
    private final OnProductDetailsClickListener productDetailsClickListener;

    public PopularItemsAdapter(List<Product> items, OnProductDetailsClickListener productDetailsClickListener) {
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
        Product product = items.get(position);
        holder.nameTV.setText(product.name);

        Picasso.get().load(ServerConnection.getInstance().host + "api/image/get_product_image/" + product.id).into(holder.imageView);

        holder.rootCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(product));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PopularItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTV;
        public ConstraintLayout rootCL;

        public PopularItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image__popular_item);
            nameTV = itemView.findViewById(R.id.item_name__popular_item);
            rootCL = itemView.findViewById(R.id.root_cl__popular_item);
        }
    }
}
