package com.example.quick_food.interfaces;

import com.example.quick_food.models.CartItemModel;
import com.example.quick_food.models.DishModel;

import java.util.List;

public interface CartListener {
    void addToCart(DishModel item);
    void removeFromCart(CartItemModel item);
    List<CartItemModel> getCartItems();
}
