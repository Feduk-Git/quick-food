package com.example.quick_food.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.R;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.FavoriteListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.DishModel;

import java.util.List;

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.SearchItemViewHolder>{
    private final List<DishModel> items;
    private final CartListener cartListener;
    private final FavoriteListener favoriteListener;
    private final OnProductDetailsClickListener productDetailsClickListener;

    public SearchItemsAdapter(List<DishModel> items, CartListener cartListener, FavoriteListener favoriteListener, OnProductDetailsClickListener productDetailsClickListener) {
        this.items = items;
        this.cartListener = cartListener;
        this.favoriteListener = favoriteListener;
        this.productDetailsClickListener = productDetailsClickListener;
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        holder.itemNameTV.setText(items.get(position).name);
        holder.itemDescTV.setText(items.get(position).description);
        holder.itemPriceTV.setText(String.valueOf(items.get(position).price));

        if (cartListener.checkCartItemExists(items.get(position))) {
            holder.addToCartTV.setText("Remove");
            holder.cartImg.setImageResource(R.drawable.ic_trash_dark);
        }
        else {
            holder.addToCartTV.setText("Add to cart");
            holder.cartImg.setImageResource(R.drawable.ic_cart2);
        }

        if (favoriteListener.checkFavoriteItemExists(items.get(position)))
            holder.favoriteButtonIV.setImageResource(R.drawable.ic_heart_filled);
        else
            holder.favoriteButtonIV.setImageResource(R.drawable.ic_heart);

        holder.addToCartCL.setOnClickListener(v -> {
            if (cartListener.removeFromCart(items.get(position))) {
                holder.addToCartTV.setText("Add to cart");
                holder.cartImg.setImageResource(R.drawable.ic_cart2);
            }
            else if (cartListener.addToCart(items.get(position))) {
                holder.addToCartTV.setText("Remove");
                holder.cartImg.setImageResource(R.drawable.ic_trash_dark);
            }
        });

        holder.cardCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(items.get(position)));

        holder.favoriteButtonIV.setOnClickListener(v -> {
            if (favoriteListener.removeFromFavorite(items.get(position)))
                holder.favoriteButtonIV.setImageResource(R.drawable.ic_heart);
            else if (favoriteListener.addToFavorite(items.get(position)))
                holder.favoriteButtonIV.setImageResource(R.drawable.ic_heart_filled);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SearchItemViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTV;
        public TextView itemDescTV;
        public TextView itemPriceTV;
        public TextView addToCartTV;
        public ConstraintLayout cardCL;
        public ConstraintLayout addToCartCL;
        public ImageView cartImg;
        public ImageView favoriteButtonIV;

        public SearchItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTV = itemView.findViewById(R.id.item_name__search_item);
            itemDescTV = itemView.findViewById(R.id.item_desc__search_item);
            itemPriceTV = itemView.findViewById(R.id.item_price_tv__search_item);
            addToCartTV = itemView.findViewById(R.id.add_to_cart_tv__search_item);
            cardCL = itemView.findViewById(R.id.card_cl__search_item);
            addToCartCL = itemView.findViewById(R.id.add_to_cart_cl__search_item);
            cartImg = itemView.findViewById(R.id.cart_img_iv__search_item);
            favoriteButtonIV = itemView.findViewById(R.id.favorite_bt_iv__search_item);
        }
    }
}
