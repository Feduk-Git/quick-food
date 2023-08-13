package com.example.quick_food.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_food.R;
import com.example.quick_food.fragments.CartFragment;
import com.example.quick_food.fragments.DishDetailsFragment;
import com.example.quick_food.fragments.PopularItemsFragment;
import com.example.quick_food.fragments.SearchFragment;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.CartItemModel;
import com.example.quick_food.models.DishModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartListener, OnProductDetailsClickListener, OnFragmentTitleChangeListener {
    private ImageView backToPreviousFragment;
    private List<CartItemModel> cartItems;
    private TextView cartItemsCountTV;
    private TextView fragmentNameTV;
    private String searchString;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cartItemsCountTV = findViewById(R.id.cart_items_count_tv__main_activity);
        fragmentNameTV = findViewById(R.id.fragment_name__activity_main);
        EditText searchBarET = findViewById(R.id.search_bar__main_activity);
        backToPreviousFragment = findViewById(R.id.previous_fragment_iv__activity_main);

        backToPreviousFragment.setOnClickListener(v -> navigateToPreviousFragment());

        ImageView searchIV = findViewById(R.id.search_button_iv__main_activity);
        searchIV.setOnClickListener(v -> {
            if (!searchBarET.getText().toString().isEmpty()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                searchBarET.clearFocus();

                searchString = searchBarET.getText().toString();
                performSearch(searchString);
            }
        });

        ConstraintLayout cartImgCL = findViewById(R.id.cart_bt_cl__activity_main);
        cartImgCL.setOnClickListener(v -> {
            CartFragment fragment = new CartFragment();
            navigateToFragment(fragment);
        });

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateButtonVisibility);
        PopularItemsFragment popularItemsFragment = new PopularItemsFragment();
        navigateToFragment(popularItemsFragment);

        searchString = getIntent().getStringExtra("searchString");
        if (searchString != null) {
            searchBarET.setText("");
            performSearch(searchString);
        }

        DishModel intentDishModel = getIntent().getParcelableExtra("dishModel");
        if (intentDishModel != null)
            onProductDetailsClick(intentDishModel);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount > 1) {
            super.onBackPressed();
        }
        else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish();
            } else {
                Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void addToCart(DishModel item) {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        cartItems.add(new CartItemModel(item));

        if (cartItems.size() == 1)
            cartItemsCountTV.setVisibility(View.VISIBLE);

        cartItemsCountTV.setText(String.valueOf(cartItems.size()));
    }

    @Override
    public void removeFromCart(CartItemModel item) {
        cartItems.remove(item);

        if (cartItems.size() == 0)
            cartItemsCountTV.setVisibility(View.GONE);
        else
            cartItemsCountTV.setText(String.valueOf(cartItems.size()));
    }

    @Override
    public List<CartItemModel> getCartItems() {
        return cartItems;
    }

    @Override
    public void onProductDetailsClick(DishModel item) {
        DishDetailsFragment fragment = new DishDetailsFragment();
        fragment.setDishModel(item);
        navigateToFragment(fragment);
    }

    @Override
    public void onTitleChanged(String title) {
        fragmentNameTV.setText(title);
    }

    public void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragment.getClass().getSimpleName();

        if (fragmentManager.findFragmentByTag(fragmentTag) != null) {
            // Если фрагмент уже существует в стеке, просто переходим к нему
            fragmentManager.popBackStackImmediate(fragmentTag, 0);
        } else {
            // Если фрагмента нет в стеке, добавляем его
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container_fl__main_activity, fragment, fragmentTag);
            ft.addToBackStack(fragmentTag);
            ft.commit();
        }

        updateButtonVisibility();
    }

    public void navigateToPreviousFragment() {
        getSupportFragmentManager().popBackStack();
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount > 1) {
            backToPreviousFragment.setVisibility(View.VISIBLE);
        } else {
            backToPreviousFragment.setVisibility(View.INVISIBLE);
        }
    }

    private void performSearch(String searchString) {
        SearchFragment fragment = new SearchFragment();
        fragment.setSearchString(searchString);
        navigateToFragment(fragment);
    }
}