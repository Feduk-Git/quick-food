package com.example.quick_food.models;

public class CartItemModel {
    private DishModel item;
    private int itemsCount;
    private double totalPrice;

    public CartItemModel(DishModel item) {
        this.item = item;
        this.itemsCount = 1;

        totalPrice = item.price * itemsCount;
    }

    public DishModel getItem() {
        return item;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public double getTotalPrice() {
        return Math.round(totalPrice * 100.0) / 100.0;
    }

    public boolean increaseCount() {
        if (itemsCount == 99)
            return false;
        itemsCount++;
        totalPrice = item.price * itemsCount;
        return true;
    }

    public boolean decreaseCount() {
        if (itemsCount == 1)
            return false;

        itemsCount--;
        totalPrice = item.price * itemsCount;
        return true;
    }
}
