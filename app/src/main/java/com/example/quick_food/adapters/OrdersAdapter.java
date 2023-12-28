package com.example.quick_food.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.R;
import com.example.quick_food.interfaces.OnOrderDetailsClickListener;
import com.example.quick_food.models.Order;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersItemViewHolder>{
    private final List<Order> items;
    private final OnOrderDetailsClickListener onOrderDetailsClickListener;

    public OrdersAdapter(List<Order> items, OnOrderDetailsClickListener onOrderDetailsClickListener) {
        this.items = items;
        this.onOrderDetailsClickListener = onOrderDetailsClickListener;
    }

    @NonNull
    @Override
    public OrdersItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersItemViewHolder holder, int position) {
        holder.orderIdTV.setText(String.valueOf(items.get(position).id));
        holder.orderDateTimeTV.setText(items.get(position).dateTime);
        holder.orderStatusTV.setText(items.get(position).status.getDisplayValue());
        holder.orderPriceTV.setText(String.valueOf(Math.round(items.get(position).price * 100.0) / 100.0));

        holder.cardCL.setOnClickListener(v -> {
            onOrderDetailsClickListener.onOrderDetailsClick(items.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class OrdersItemViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTV;
        public TextView orderDateTimeTV;
        public TextView orderStatusTV;
        public TextView orderPriceTV;
        public ConstraintLayout cardCL;

        public OrdersItemViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTV = itemView.findViewById(R.id.id_tv__order_item);
            orderDateTimeTV = itemView.findViewById(R.id.date_time_tv__order_item);
            orderStatusTV = itemView.findViewById(R.id.status_tv__order_item);
            orderPriceTV = itemView.findViewById(R.id.price_tv__order_item);
            cardCL = itemView.findViewById(R.id.card_cl__order_item);
        }
    }
}
