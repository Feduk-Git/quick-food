package com.example.quick_food.interfaces;

import com.example.quick_food.models.CartItemModel;
import com.example.quick_food.models.DishModel;

import java.util.List;

public interface CartListener {
    boolean addToCart(DishModel item);
    boolean removeFromCart(DishModel item);
    List<CartItemModel> getCartItems();
    boolean checkCartItemExists(DishModel item);
    void saveCartItems();
    void loadCartItems();
}
