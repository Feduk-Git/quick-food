package com.example.quick_food.interfaces;

import com.example.quick_food.models.Order;

public interface OrderListener {
    void orderConfirmation();
    void confirmOrder(Order order);
}
