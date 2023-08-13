package com.example.quick_food.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quick_food.R;

import java.util.List;

public class DishImgViewPagerAdapter extends RecyclerView.Adapter<DishImgViewPagerAdapter.DishImgViewViewHolder> {
    private List<String> imgList;

    public DishImgViewPagerAdapter(List<String> imgList) {
        this.imgList = imgList;
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

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public static class DishImgViewViewHolder extends RecyclerView.ViewHolder {
        public DishImgViewViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
