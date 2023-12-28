package com.example.quick_food.models;

public enum OrderStatus {
    WAITING_CONFIRM("Waiting confirm"),
    COOKING("Cooking"),
    DELIVERING("Delivering"),
    CLOSED("Closed"),
    CANCELED("Canceled");

    private final String displayValue;

    OrderStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
