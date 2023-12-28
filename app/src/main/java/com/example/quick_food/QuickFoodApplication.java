package com.example.quick_food;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.example.quick_food.models.SharedViewModel;

public class QuickFoodApplication extends Application {
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(SharedViewModel.class);
    }

    public SharedViewModel getSharedViewModel() {
        return sharedViewModel;
    }
}
