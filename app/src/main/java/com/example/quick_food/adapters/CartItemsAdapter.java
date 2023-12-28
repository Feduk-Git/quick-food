package com.example.quick_food.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.R;
import com.example.quick_food.ServerConnection;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.CartItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {
    private final List<CartItem> items;
    private final CartListener cartListener;
    private final OnItemRemoveListener itemRemoveListener;
    private final OnCartIsEmptyListener cartIsEmptyListener;
    private final OnProductDetailsClickListener productDetailsClickListener;
    private final List<OnCountChangeClickListener> onCountChangeClickListeners = new ArrayList<>();

    public CartItemsAdapter(List<CartItem> items, CartListener cartListener,
                            OnItemRemoveListener itemRemoveListener, OnCartIsEmptyListener cartIsEmptyListener, OnProductDetailsClickListener productDetailsClickListener) {
        this.items = items;
        this.cartListener = cartListener;
        this.itemRemoveListener = itemRemoveListener;
        this.cartIsEmptyListener = cartIsEmptyListener;
        this.productDetailsClickListener = productDetailsClickListener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = items.get(position);
        holder.nameTV.setText(cartItem.getProduct().name);
        holder.descTV.setText(cartItem.getProduct().description);
        holder.priceTV.setText(String.valueOf(cartItem.getProduct().price));
        holder.totalPriceTV.setText(String.valueOf(cartItem.getTotalPrice()));
        holder.countTV.setText(String.valueOf(cartItem.getItemsCount()));

        Picasso.get().load(ServerConnection.getInstance().host + "api/image/get_product_image/" + cartItem.getProduct().id).into(holder.itemImg);

        holder.minusIV.setOnClickListener(v -> {
            if (cartItem.decreaseCount()) {
                holder.countTV.setText(String.valueOf(cartItem.getItemsCount()));
                holder.totalPriceTV.setText(String.valueOf(cartItem.getTotalPrice()));

                for (OnCountChangeClickListener listener : onCountChangeClickListeners) {
                    listener.onDecreaseCountClicked(holder.getAdapterPosition());
                }
            }
        });

        holder.plusIV.setOnClickListener(v -> {
            if (cartItem.increaseCount()) {
                holder.countTV.setText(String.valueOf(cartItem.getItemsCount()));
                holder.totalPriceTV.setText(String.valueOf(cartItem.getTotalPrice()));

                for (OnCountChangeClickListener listener : onCountChangeClickListeners) {
                    listener.onIncreaseCountClicked(holder.getAdapterPosition());
                }
            }
        });

        holder.removeBT.setOnClickListener(v -> {
            //передается holder.getAdapterPosition() вместо position из-за проблем с некоректной итерацией по элементам
            //которые возникают из-за метода notifyItemRemoved()
            if (cartListener.removeFromCart(cartItem.getProduct())) {
                notifyItemRemoved(holder.getAdapterPosition());
                itemRemoveListener.onItemRemoved(cartItem.getTotalPrice());
                if (items.size() == 0)
                    cartIsEmptyListener.onCartIsEmpty();
            }
        });

        holder.cardCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(cartItem.getProduct()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addCountChangeClickListener(OnCountChangeClickListener listener) {
        onCountChangeClickListeners.add(listener);
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTV;
        public TextView descTV;
        public TextView priceTV;
        public TextView totalPriceTV;
        public TextView countTV;
        public ImageView minusIV;
        public ImageView plusIV;
        public ImageView itemImg;
        public Button removeBT;
        public ConstraintLayout cardCL;

        public CartItemViewHolder(View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.item_name__cart_item);
            descTV = itemView.findViewById(R.id.item_desc__cart_item);
            priceTV = itemView.findViewById(R.id.item_price_tv__cart_item);
            totalPriceTV = itemView.findViewById(R.id.total_price_tv__cart_item);
            countTV = itemView.findViewById(R.id.count_tv__cart_item);
            minusIV = itemView.findViewById(R.id.minus_iv__cart_item);
            plusIV = itemView.findViewById(R.id.plus_iv__cart_item);
            itemImg = itemView.findViewById(R.id.item_image_iv__cart_item);
            removeBT = itemView.findViewById(R.id.remove_bt__cart_item);
            cardCL = itemView.findViewById(R.id.card_cl__cart_item);
        }
    }

    public interface OnCountChangeClickListener {
        void onIncreaseCountClicked(int position);
        void onDecreaseCountClicked(int position);
    }

    public interface OnItemRemoveListener {
        void onItemRemoved(double itemPrice);
    }

    public interface OnCartIsEmptyListener {
        void onCartIsEmpty();
    }
}
