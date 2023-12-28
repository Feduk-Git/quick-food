package com.example.quick_food.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductCount {
    private int productId;
    private int count;

    @JsonCreator
    public ProductCount(
            @JsonProperty("productId") int productId,
            @JsonProperty("count") int count) {
        this.productId = productId;
        this.count = count;
    }

    public int getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }
}
