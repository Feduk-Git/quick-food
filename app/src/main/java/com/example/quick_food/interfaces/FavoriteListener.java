package com.example.quick_food.interfaces;

import com.example.quick_food.models.Product;

public interface FavoriteListener {
    boolean addToFavorite(Product item);
    boolean removeFromFavorite(Product item);
}
