package com.example.quick_food.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.R;
import com.example.quick_food.activities.MainActivity;
import com.example.quick_food.fragments.DishDetailsFragment;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.DishModel;

import java.util.List;

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.SearchItemViewHolder>{
    private final List<DishModel> items;
    private final MainActivity mainActivity;
    private CartListener cartListener;
    private OnProductDetailsClickListener productDetailsClickListener;

    public SearchItemsAdapter(List<DishModel> items, MainActivity mainActivity, CartListener cartListener, OnProductDetailsClickListener productDetailsClickListener) {
        this.items = items;
        this.mainActivity = mainActivity;
        this.cartListener = cartListener;
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

        holder.addToCartCL.setOnClickListener(v -> cartListener.addToCart(items.get(position)));
        holder.cardCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(items.get(position)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SearchItemViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTV;
        public TextView itemDescTV;
        public TextView addToCartTV;
        public ConstraintLayout cardCL;
        public ConstraintLayout addToCartCL;

        public SearchItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTV = itemView.findViewById(R.id.item_name__search_item);
            itemDescTV = itemView.findViewById(R.id.item_desc__search_item);
            addToCartTV = itemView.findViewById(R.id.add_to_cart_tv__search_item);
            cardCL = itemView.findViewById(R.id.card_cl__search_item);
            addToCartCL = itemView.findViewById(R.id.add_to_cart_cl__search_item);
        }
    }
}
