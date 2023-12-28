package com.example.quick_food.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
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

import com.example.quick_food.ServerConnection;
import com.example.quick_food.fragments.OrderDetailsFragment;
import com.example.quick_food.fragments.OrdersFragment;
import com.example.quick_food.QuickFoodApplication;
import com.example.quick_food.R;
import com.example.quick_food.adapters.CartItemsAdapter;
import com.example.quick_food.fragments.CartFragment;
import com.example.quick_food.fragments.DishDetailsFragment;
import com.example.quick_food.fragments.FavoriteFragment;
import com.example.quick_food.fragments.PopularItemsFragment;
import com.example.quick_food.fragments.SearchFragment;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.FavoriteListener;
import com.example.quick_food.interfaces.OnCategoryDetailsClickListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnOrderDetailsClickListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.interfaces.OrderListener;
import com.example.quick_food.models.CartItem;
import com.example.quick_food.models.Product;
import com.example.quick_food.models.Order;
import com.example.quick_food.models.SharedViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartListener, OnProductDetailsClickListener,
        OnFragmentTitleChangeListener, OnCategoryDetailsClickListener, OnOrderDetailsClickListener, FavoriteListener, OrderListener, CartItemsAdapter.OnCountChangeClickListener {

//region [FIELDS]
    private ImageView backToPreviousFragment;
    private List<CartItem> cartItems;
    private List<Product> favoriteItems;
    private List<Order> orders;
    private TextView cartItemsCountTV;
    private TextView favoriteItemsCountTV;
    private TextView ordersCountTV;
    private TextView fragmentNameTV;
    private String searchString;
    private long backPressedTime = 0;
    private boolean isNotificationAnimationEnded = true;

    private SharedViewModel sharedVM;
    private boolean newSearch;
//endregion

//region [MainActivityOverride]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartItemsCountTV = findViewById(R.id.cart_items_count_tv__main_activity);
        favoriteItemsCountTV = findViewById(R.id.favorite_items_count_tv__main_activity);
        ordersCountTV = findViewById(R.id.orders_count_tv__main_activity);
        fragmentNameTV = findViewById(R.id.fragment_name__activity_main);
        EditText searchBarET = findViewById(R.id.search_bar__main_activity);
        backToPreviousFragment = findViewById(R.id.previous_fragment_iv__activity_main);

        sharedVM = ((QuickFoodApplication) getApplication()).getSharedViewModel();
        loadFavoriteList();
        loadCartItems();
        loadOrdersList();

        backToPreviousFragment.setOnClickListener(v -> navigateToPreviousFragment());

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

        ConstraintLayout ordersListImgCL = findViewById(R.id.orders_list_bt_cl__activity_main);
        ordersListImgCL.setOnClickListener(v -> {
            OrdersFragment fragment = new OrdersFragment();
            navigateToFragment(fragment);
        });

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateButtonVisibility);
        PopularItemsFragment popularItemsFragment = new PopularItemsFragment();
        navigateToFragment(popularItemsFragment);

        searchString = getIntent().getStringExtra("searchString");
        if (searchString != null) {
            performSearch(searchString);
        }

        Product intentProduct = getIntent().getParcelableExtra("dishModel");
        if (intentProduct != null)
            onProductDetailsClick(intentProduct);
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
//endregion

//region [MainActivityUI]
    //region [ProductDetailsClickListener]
        @Override
        public void onProductDetailsClick(Product item) {
            DishDetailsFragment fragment = new DishDetailsFragment();
            fragment.setDishModel(item);
            navigateToFragment(fragment);
        }
    //endregion

    //region [OnFragmentTitleChangedListener]
        @Override
        public void onTitleChanged(String title) {
            fragmentNameTV.setText(title);
        }

        @Override
        public void onCategoryDetailsClicked(String categoryName) {
            performSearch(categoryName);
        }
    //endregion

    //region [OnOrderDetailsClickListener]
        @Override
        public void onOrderDetailsClick(Order order) {
            OrderDetailsFragment fragment = new OrderDetailsFragment(order);
            navigateToFragment(fragment);
        }
    //endregion

    //region [FragmentNavigation]
    public void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragment.getClass().getSimpleName();

        if (fragmentManager.findFragmentByTag(fragmentTag) != null && !newSearch) {
            // Если фрагмент уже существует в стеке, просто переходим к нему
            fragmentManager.popBackStackImmediate(fragmentTag, 0);
        } else {
            if (fragmentManager.findFragmentByTag(fragmentTag) != null)
                fragmentManager.popBackStackImmediate(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        newSearch = true;
        SearchFragment fragment = new SearchFragment();
        fragment.setSearchString(searchString);
        navigateToFragment(fragment);
        newSearch = false;
    }
    //endregion

    //region [Animation]
    private void startInAppNotificationAnimation(Product item, boolean remove) {
        ImageView resultImg = findViewById(R.id.notification_result_img__activity_main);
        ImageView notificationImg = findViewById(R.id.notification_img__activity_main);
        TextView resultText = findViewById(R.id.notification_result_tv__activity_main);
        TextView notificationText = findViewById(R.id.notification_tv__activity_main);
        CardView notificationCard = findViewById(R.id.notification_card__activity_main);

        Picasso.get().load(ServerConnection.getInstance().host + "api/image/get_product_image/" + item.id).into(notificationImg);
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
    //endregion

    private void setupCartItemsCountTV() {
        if (cartItems != null && cartItems.size() > 0) {
            cartItemsCountTV.setVisibility(View.VISIBLE);
            cartItemsCountTV.setText(String.valueOf(cartItems.size()));
        }
        else
            cartItemsCountTV.setVisibility(View.INVISIBLE);
    }

    private void setupFavoritesCountTV() {
        if (favoriteItems != null && favoriteItems.size() > 0) {
            favoriteItemsCountTV.setVisibility(View.VISIBLE);
            favoriteItemsCountTV.setText(String.valueOf(favoriteItems.size()));
        }
        else
            favoriteItemsCountTV.setVisibility(View.INVISIBLE);
    }

    private void setupOrdersCountTV() {
        if (orders != null && orders.size() > 0) {
            ordersCountTV.setVisibility(View.VISIBLE);
            ordersCountTV.setText(String.valueOf(orders.size()));
        }
        else
            ordersCountTV.setVisibility(View.INVISIBLE);
    }
//endregion

//region [CartListener]
    @Override
    public boolean addToCart(Product item) {
        if (isNotificationAnimationEnded && sharedVM.addToCart(item)) {
            startInAppNotificationAnimation(item, false);
            setupCartItemsCountTV();
            saveCartItems();
            return true;
        } else
            return false;
    }

    @Override
    public boolean removeFromCart(Product item) {
        if (isNotificationAnimationEnded && sharedVM.removeFromCart(item)) {
            startInAppNotificationAnimation(item, true);
            setupCartItemsCountTV();
            saveCartItems();
            return true;
        } else
            return false;
    }

    @Override
    public void onIncreaseCountClicked(int position) {
        saveCartItems();
    }

    @Override
    public void onDecreaseCountClicked(int position) {
        saveCartItems();
    }
//endregion

//region [FavoriteListener]
    @Override
    public boolean addToFavorite(Product item) {
        if (isNotificationAnimationEnded && sharedVM.addToFavorite(item)) {
            startInAppNotificationAnimation(item, false);
            setupFavoritesCountTV();
            saveFavoriteList();
            return true;
        } else
            return false;
    }

    @Override
    public boolean removeFromFavorite(Product item) {
        if (isNotificationAnimationEnded && sharedVM.removeFromFavorite(item)) {
            startInAppNotificationAnimation(item, true);
            setupFavoritesCountTV();
            saveFavoriteList();
            return true;
        } else
            return false;
    }
//endregion

//region [SaveToSharedPreferences]
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

    public void loadCartItems() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("cartItems", null);
        sharedVM.loadCartItems(gson.fromJson(json, new TypeToken<List<CartItem>>(){}.getType()));
        cartItems = sharedVM.getCartItems();
        setupCartItemsCountTV();
    }

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

    public void loadFavoriteList() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("favoriteItems", null);
        sharedVM.loadFavoriteItems(gson.fromJson(json, new TypeToken<List<Product>>(){}.getType()));
        favoriteItems = sharedVM.getFavoriteList();
        setupFavoritesCountTV();
    }

    public void saveOrdersList() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Преобразование списка в json
        Gson gson = new Gson();
        String json = gson.toJson(orders);

        // Сохранение JSON-строки в SharedPreferences
        editor.putString("ordersList", json);

        editor.apply();
    }

    public void loadOrdersList() {
        // Получение доступа к SharedPreferences
        SharedPreferences sp = getSharedPreferences("Shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("ordersList", null);
        sharedVM.loadOrdersList(gson.fromJson(json, new TypeToken<LinkedList<Order>>(){}.getType()));
        orders = sharedVM.getOrders();
        saveOrdersList();
        setupOrdersCountTV();
    }
//endregion

//region [OrderListener]
    @Override
    public void orderConfirmation() {
        Intent intent = new Intent(this, OrderActivity.class);
        sharedVM.setOrderListener(this);
        startActivity(intent);
    }

    private Order order;
    @Override
    public void confirmOrder(Order order) {
        Thread thread = new Thread(() -> {
            this.order = ServerConnection.getInstance().createNewOrder(order);

            runOnUiThread(() -> {
                sharedVM.addOrder(this.order);
                setupOrdersCountTV();
                saveOrdersList();

                cartItems = sharedVM.getCartItems();
                setupCartItemsCountTV();
                saveCartItems();
            });
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//endregion
}