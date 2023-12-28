package com.example.quick_food.models;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Category {
    public int id;
    public String name;

    public Category () {}

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
