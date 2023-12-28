package com.example.quick_food.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quick_food.ServerConnection;
import com.example.quick_food.adapters.PopularItemsAdapter;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.Category;
import com.example.quick_food.models.Product;
import com.example.quick_food.R;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.PopularItemSpacingDecoration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity implements OnProductDetailsClickListener {
    private List<Product> itemList;
    private LinearLayout ll;
    private Intent intent;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_intro);

        intent = new Intent(IntroActivity.this, MainActivity.class);
        TextView skipTV = findViewById(R.id.skip_tv__intro_activity);
        ImageView searchIV = findViewById(R.id.search_button_iv__intro_activity);
        EditText searchBarET = findViewById(R.id.search_bar__intro_activity);

        new Thread(() -> {
            itemList = ServerConnection.getInstance().getPopularProducts(10);

            runOnUiThread(() -> {
                setupRecyclerView();
                int elementsPerPage = 3;
                setupDots(getPagesCount(elementsPerPage));
            });
        }).start();

        skipTV.setOnClickListener(v -> {
            startActivity(intent);
            finish();
        });

        searchIV.setOnClickListener(v -> {
            String searchString = searchBarET.getText().toString();
            if (!searchString.isEmpty()) {
                intent.putExtra("searchString", searchString);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onProductDetailsClick(Product item) {
        intent.putExtra("dishModel", item);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.popular_items_rv__intro_activity);

        // Создание адаптера и установка данных
        PopularItemsAdapter adapter = new PopularItemsAdapter(itemList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new PopularItemSpacingDecoration(DpToPixels.convert(14, this), DpToPixels.convert(36, this)));

        // Добавление слушателя прокрутки RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int currentPage = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() / 3;
                updateTabLayout(currentPage);
            }
        });
    }

    private void setupDots(int pageCount) {
        ll = findViewById(R.id.dots_layout__intro_activity);

        for (int i = 0; i < pageCount; i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params;
            if (i == 0)
                params = getParamsForDot(7,7);
            else
                params = getParamsForDot(5,5);

            view.setLayoutParams(params);
            ll.addView(view);
        }
    }

    private void updateTabLayout(int currentPage) {
        for (int i = 0; i < ll.getChildCount(); i++) {
            if (i == currentPage) {
                ll.getChildAt(i).setBackgroundResource(R.drawable.dot_active);
                ll.getChildAt(i).setLayoutParams(getParamsForDot(7,7));
            }
            else {
                ll.getChildAt(i).setBackgroundResource(R.drawable.dot_inactive);
                ll.getChildAt(i).setLayoutParams(getParamsForDot(5,5));
            }
        }
    }

    private int getPagesCount(int elementsPerPage) {
        int pagesCount;

        pagesCount = itemList.size() / elementsPerPage;

        if (itemList.size() % elementsPerPage != 0)
            pagesCount++;

        return pagesCount;
    }

    private LinearLayout.LayoutParams getParamsForDot(int widthInDp, int heightInDp) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DpToPixels.convert(widthInDp, this), DpToPixels.convert(heightInDp, this));
        params.setMargins(0,0,DpToPixels.convert(7, this),0);
        return params;
    }
}