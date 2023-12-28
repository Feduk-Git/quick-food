package com.example.quick_food.models;

import java.util.List;

public class Order {
    public int id;
    public String name;
    public String surname;
    public String address;
    public String phoneNumber;
    public String description;
    public OrderStatus status;
    public String dateTime;
    public List<CartItem> cartItems;
    public City city;
    public double price;
}
