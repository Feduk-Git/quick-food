package com.example.quick_food.interfaces;

import com.example.quick_food.models.DishModel;

import java.util.List;

public interface FavoriteListener {
    boolean addToFavorite(DishModel item);
    boolean removeFromFavorite(DishModel item);
    List<DishModel> getFavoriteList();
    boolean checkFavoriteItemExists(DishModel item);
    void saveFavoriteList();
    void loadFavoriteList();
}
