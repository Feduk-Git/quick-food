package com.example.quick_food.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_food.R;
import com.example.quick_food.fragments.CartFragment;
import com.example.quick_food.fragments.DishDetailsFragment;
import com.example.quick_food.fragments.FavoriteFragment;
import com.example.quick_food.fragments.PopularItemsFragment;
import com.example.quick_food.fragments.SearchFragment;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.FavoriteListener;
import com.example.quick_food.interfaces.OnCategoryDetailsClickListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.CartItemModel;
import com.example.quick_food.models.DishModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartListener, OnProductDetailsClickListener,
        OnFragmentTitleChangeListener, OnCategoryDetailsClickListener, FavoriteListener {
    private ImageView backToPreviousFragment;
    private List<CartItemModel> cartItems;
    private List<DishModel> favoriteItems;
    private TextView cartItemsCountTV;
    private TextView favoriteItemsCountTV;
    private TextView fragmentNameTV;
    private String searchString;
    private long backPressedTime = 0;
    private boolean isNotificationAnimationEnded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cartItemsCountTV = findViewById(R.id.cart_items_count_tv__main_activity);
        favoriteItemsCountTV = findViewById(R.id.favorite_items_count_tv__main_activity);
        fragmentNameTV = findViewById(R.id.fragment_name__activity_main);
        EditText searchBarET = findViewById(R.id.search_bar__main_activity);
        backToPreviousFragment = findViewById(R.id.previous_fragment_iv__activity_main);

        backToPreviousFragment.setOnClickListener(v -> navigateToPreviousFragment());
        loadFavoriteList();
        loadCartItems();

        ImageView searchIV = findViewById(R.id.search_button_iv__main_activity);
        searchIV.setOnClickListener(v -> {
            if (!searchBarET.getText().toString().isEmpty()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                searchBarET.clearFocus();

                searchString = searchBarET.getText().toString();
                searchBarET.setText("");
                performSearch(searchString);
            }
        });

        ConstraintLayout cartImgCL = findViewById(R.id.cart_bt_cl__activity_main);
        cartImgCL.setOnClickListener(v -> {
            CartFragment fragment = new CartFragment();
            navigateToFragment(fragment);
        });

        ConstraintLayout favoriteImgCl = findViewById(R.id.favorite_bt_cl__activity_main);
        favoriteImgCl.setOnClickListener(v -> {
            FavoriteFragment fragment = new FavoriteFragment();
            navigateToFragment(fragment);
        });

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateButtonVisibility);
        PopularItemsFragment popularItemsFragment = new PopularItemsFragment();
        navigateToFragment(popularItemsFragment);

        searchString = getIntent().getStringExtra("searchString");
        if (searchString != null) {
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
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finish();
            } else {
                Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean addToCart(DishModel item) {
        if (cartItems == null)
            cartItems = new ArrayList<>();

        if (!checkCartItemExists(item) && isNotificationAnimationEnded) {
            cartItems.add(new CartItemModel(item));
            startInAppNotificationAnimation(item, false);
        } else
            return false;

        setupCartItemsCountTV();
        saveCartItems();

        return true;
    }

    @Override
    public boolean removeFromCart(DishModel item) {
        CartItemModel itemToRemove = null;

        if (cartItems == null)
            return false;

        for (CartItemModel cartItem : cartItems) {
            if (cartItem.getItem().name.equals(item.name))
                itemToRemove = cartItem;
        }

        if (itemToRemove != null && isNotificationAnimationEnded) {
            cartItems.remove(itemToRemove);
            startInAppNotificationAnimation(item, true);
        } else
            return false;

        setupCartItemsCountTV();
        saveCartItems();

        return true;
    }

    @Override
    public List<CartItemModel> getCartItems() {
        return cartItems;
    }

    @Override
    public boolean checkCartItemExists(DishModel item) {
        if (cartItems != null) {
            for (CartItemModel cartItem : cartItems) {
                if (cartItem.getItem().name.equals(item.name))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void saveCartItems() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Преобразование списка в json
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);

        // Сохранение JSON-строки в SharedPreferences
        editor.putString("cartItems", json);
        editor.apply();
    }

    @Override
    public void loadCartItems() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("cartItems", null);
        cartItems = gson.fromJson(json, new TypeToken<List<CartItemModel>>(){}.getType());
        setupCartItemsCountTV();
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

    @Override
    public void onCategoryDetailsClicked(String categoryName) {
        performSearch(categoryName);
    }

    @Override
    public boolean addToFavorite(DishModel item) {
        if (favoriteItems == null)
            favoriteItems = new ArrayList<>();

        if (!checkFavoriteItemExists(item) && isNotificationAnimationEnded) {
            favoriteItems.add(item);
            startInAppNotificationAnimation(item, false);
        } else
            return false;

        setupFavoritesCountTV();

        saveFavoriteList();
        return true;
    }

    @Override
    public boolean removeFromFavorite(DishModel item) {
        DishModel itemToRemove = null;

        if (favoriteItems == null)
            return false;

        for (DishModel favoriteItem : favoriteItems) {
            if (favoriteItem.name.equals(item.name))
                itemToRemove = favoriteItem;
        }

        if (itemToRemove != null && isNotificationAnimationEnded) {
            favoriteItems.remove(itemToRemove);
            startInAppNotificationAnimation(item, true);
        } else
            return false;

        setupFavoritesCountTV();

        saveFavoriteList();
        return true;
    }

    @Override
    public List<DishModel> getFavoriteList() {
        return favoriteItems;
    }

    @Override
    public boolean checkFavoriteItemExists(DishModel item) {
        if (favoriteItems != null) {
            for (DishModel favoriteItem : favoriteItems) {
                if (favoriteItem.name.equals(item.name))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void saveFavoriteList() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Преобразование списка в json
        Gson gson = new Gson();
        String json = gson.toJson(favoriteItems);

        // Сохранение JSON-строки в SharedPreferences
        editor.putString("favoriteItems", json);
        editor.apply();
    }

    @Override
    public void loadFavoriteList() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("favoriteItems", null);
        favoriteItems = gson.fromJson(json, new TypeToken<List<DishModel>>(){}.getType());
        setupFavoritesCountTV();
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

    private void startInAppNotificationAnimation(DishModel item, boolean remove) {
        ImageView resultImg = findViewById(R.id.notification_result_img__activity_main);
        ImageView notificationImg = findViewById(R.id.notification_img__activity_main);
        TextView resultText = findViewById(R.id.notification_result_tv__activity_main);
        TextView notificationText = findViewById(R.id.notification_tv__activity_main);
        CardView notificationCard = findViewById(R.id.notification_card__activity_main);

        notificationImg.setImageResource(R.drawable.img2);
        notificationText.setText(item.name);

        if (remove) {
            resultImg.setImageResource(R.drawable.ic_cancel);
            resultText.setText("Removed");
        } else {
            resultImg.setImageResource(R.drawable.ic_confirm);
            resultText.setText("Added");
        }

        Animation slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);

        notificationCard.setVisibility(View.VISIBLE);
        notificationCard.startAnimation(slideDownAnimation);
        isNotificationAnimationEnded = false;

        new Handler().postDelayed(() -> {
            notificationCard.startAnimation(slideUpAnimation);

            new Handler().postDelayed(() -> {
                notificationCard.setVisibility(View.GONE);
                isNotificationAnimationEnded = true;
            }, slideDownAnimation.getDuration());
        }, slideDownAnimation.getDuration() + 700);
    }

    private void setupFavoritesCountTV() {
        if (favoriteItems != null && favoriteItems.size() > 0) {
            favoriteItemsCountTV.setVisibility(View.VISIBLE);
            favoriteItemsCountTV.setText(String.valueOf(favoriteItems.size()));
        }
        else
            favoriteItemsCountTV.setVisibility(View.INVISIBLE);
    }

    private void setupCartItemsCountTV() {
        if (cartItems != null && cartItems.size() > 0) {
            cartItemsCountTV.setVisibility(View.VISIBLE);
            cartItemsCountTV.setText(String.valueOf(cartItems.size()));
        }
        else
            cartItemsCountTV.setVisibility(View.INVISIBLE);
    }
}