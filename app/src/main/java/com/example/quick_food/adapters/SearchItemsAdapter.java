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
import com.example.quick_food.ServerConnection;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.FavoriteListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.Product;
import com.example.quick_food.models.SharedViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.SearchItemViewHolder>{
    private final List<Product> items;
    private final CartListener cartListener;
    private final FavoriteListener favoriteListener;
    private final OnProductDetailsClickListener productDetailsClickListener;
    private final SharedViewModel sharedVM;

    public SearchItemsAdapter(List<Product> items, CartListener cartListener, FavoriteListener favoriteListener,
                              OnProductDetailsClickListener productDetailsClickListener, SharedViewModel sharedVM) {
        this.items = items;
        this.cartListener = cartListener;
        this.favoriteListener = favoriteListener;
        this.productDetailsClickListener = productDetailsClickListener;
        this.sharedVM = sharedVM;
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
        Product product = items.get(position);
        holder.itemNameTV.setText(product.name);
        holder.itemDescTV.setText(product.description);
        holder.itemPriceTV.setText(String.valueOf(product.price));
        Picasso.get().load(ServerConnection.getInstance().host + "api/image/get_product_image/" + product.id).into(holder.productImg);

        if (sharedVM.checkCartItemExists(product)) {
            holder.addToCartTV.setText("Remove");
            holder.cartImg.setImageResource(R.drawable.ic_trash_dark);
        }
        else {
            holder.addToCartTV.setText("Add to cart");
            holder.cartImg.setImageResource(R.drawable.ic_cart2);
        }

        if (sharedVM.checkFavoriteItemExists(product))
            holder.favoriteButtonIV.setImageResource(R.drawable.ic_heart_filled);
        else
            holder.favoriteButtonIV.setImageResource(R.drawable.ic_heart);

        holder.addToCartCL.setOnClickListener(v -> {
            if (cartListener.removeFromCart(product)) {
                holder.addToCartTV.setText("Add to cart");
                holder.cartImg.setImageResource(R.drawable.ic_cart2);
            }
            else if (cartListener.addToCart(product)) {
                holder.addToCartTV.setText("Remove");
                holder.cartImg.setImageResource(R.drawable.ic_trash_dark);
            }
        });

        holder.cardCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(product));

        holder.favoriteButtonIV.setOnClickListener(v -> {
            if (favoriteListener.removeFromFavorite(product))
                holder.favoriteButtonIV.setImageResource(R.drawable.ic_heart);
            else if (favoriteListener.addToFavorite(product))
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
        public ImageView productImg;

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
            productImg = itemView.findViewById(R.id.item_image_iv__search_item);
        }
    }
}
