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
import com.example.quick_food.activities.MainActivity;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailsDishesListAdapter extends RecyclerView.Adapter<OrderDetailsDishesListAdapter.OrderDetailsDishViewHolder>{
    private final List<CartItem> items;
    private OnProductDetailsClickListener productDetailsClickListener;

    public OrderDetailsDishesListAdapter(List<CartItem> items, OnProductDetailsClickListener productDetailsClickListener) {
        this.items = items;
        this.productDetailsClickListener = productDetailsClickListener;
    }

    @NonNull
    @Override
    public OrderDetailsDishesListAdapter.OrderDetailsDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderDetailsDishesListAdapter.OrderDetailsDishViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_dish_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsDishesListAdapter.OrderDetailsDishViewHolder holder, int position) {
        CartItem cartItem = items.get(position);
        holder.productName.setText(cartItem.getProduct().name);
        holder.productDesc.setText(cartItem.getProduct().description);
        holder.productPrice.setText(String.valueOf(cartItem.getProduct().price));
        holder.totalPrice.setText(String.valueOf(cartItem.getTotalPrice()));
        holder.productsCount.setText(String.valueOf(cartItem.getItemsCount()));

        Picasso.get().load(ServerConnection.getInstance().host + "api/image/get_product_image/" + cartItem.getProduct().id).into(holder.productImg);

        holder.cardCL.setOnClickListener(v -> productDetailsClickListener.onProductDetailsClick(cartItem.getProduct()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class OrderDetailsDishViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productDesc;
        public TextView productPrice;
        public TextView totalPrice;
        public TextView productsCount;
        public ImageView productImg;
        public ConstraintLayout cardCL;

        public OrderDetailsDishViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.item_name__order_details_dish_item);
            productDesc = itemView.findViewById(R.id.item_desc__order_details_dish_item);
            productPrice = itemView.findViewById(R.id.item_price_tv__order_details_dish_item);
            totalPrice = itemView.findViewById(R.id.total_price_tv__order_details_dish_item);
            productsCount = itemView.findViewById(R.id.count_tv__order_details_dish_item);
            productImg = itemView.findViewById(R.id.item_image_iv__order_details_dish_item);
            cardCL = itemView.findViewById(R.id.card_cl__order_details_dish_item);
        }
    }
}
