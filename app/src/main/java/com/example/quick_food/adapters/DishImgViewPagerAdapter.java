package com.example.quick_food.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quick_food.R;
import com.example.quick_food.ServerConnection;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DishImgViewPagerAdapter extends RecyclerView.Adapter<DishImgViewPagerAdapter.DishImgViewViewHolder> {
    private List<String> imagesURLs;
    private int productId;

    public DishImgViewPagerAdapter(int productId, List<String> imagesURLs) {
        this.productId = productId;
        this.imagesURLs = imagesURLs;
    }

    @NonNull
    @Override
    public DishImgViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishImgViewViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_image_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull DishImgViewViewHolder holder, int position) {
        Picasso.get().load(ServerConnection.getInstance().host + "api/image/get_product_image/" + productId).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesURLs.size();
    }

    public static class DishImgViewViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public DishImgViewViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image__dish_image_item);
        }
    }
}
