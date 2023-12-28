package com.example.quick_food.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem {
    private Product product;
    private int itemsCount;
    private double totalPrice;

    @JsonCreator
    public CartItem(@JsonProperty("productDto") Product product, @JsonProperty("itemsCount") int itemsCount, @JsonProperty("price") double totalPrice) {
        this.product = product;
        this.itemsCount = itemsCount;
        this.totalPrice = totalPrice;
    }

    public CartItem(Product product) {
        this.product = product;
        this.itemsCount = 1;

        totalPrice = product.price * itemsCount;
    }

    public Product getProduct() {
        return product;
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
        totalPrice = product.price * itemsCount;
        return true;
    }

    public boolean decreaseCount() {
        if (itemsCount == 1)
            return false;

        itemsCount--;
        totalPrice = product.price * itemsCount;
        return true;
    }
}
