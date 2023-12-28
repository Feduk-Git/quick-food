package com.example.quick_food.interfaces;

import com.example.quick_food.models.Product;

public interface CartListener {
    boolean addToCart(Product item);
    boolean removeFromCart(Product item);
}
