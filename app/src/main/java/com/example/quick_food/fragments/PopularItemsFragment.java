package com.example.quick_food.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quick_food.R;
import com.example.quick_food.ServerConnection;
import com.example.quick_food.activities.MainActivity;
import com.example.quick_food.adapters.CategoriesAdapter;
import com.example.quick_food.adapters.PopularItemsGridAdapter;
import com.example.quick_food.interfaces.OnCategoryDetailsClickListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.Category;
import com.example.quick_food.models.Product;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.PopularItemGridSpacingDecoration;
import com.example.quick_food.utils.PopularItemSpacingDecoration;

import java.util.ArrayList;
import java.util.List;

public class PopularItemsFragment extends Fragment {
    private List<Category> popularCategoriesList;
    private List<Product> popularProductsList;
    private OnProductDetailsClickListener productDetailsClickListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private OnCategoryDetailsClickListener categoryDetailsClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            productDetailsClickListener = (OnProductDetailsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnProductDetailsClickListener");
        }
        try {
            fragmentTitleChangeListener = (OnFragmentTitleChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnFragmentTitleChangeListener");
        }
        try {
            categoryDetailsClickListener = (OnCategoryDetailsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnCategoryDetailsClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView viewAllTV = view.findViewById(R.id.view_all_tv__fragment_popular_items);
        viewAllTV.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity)getActivity();
            if (mainActivity != null) {
                CategoriesFragment fragment = new CategoriesFragment();
                mainActivity.navigateToFragment(fragment);
            }
        });

        new Thread(() -> {
            popularProductsList = ServerConnection.getInstance().getPopularProducts(12);
            popularCategoriesList = ServerConnection.getInstance().getPopularCategories(5);
            getActivity().runOnUiThread(() -> {
                setupPopularItemsRecyclerView(view);
                setupCategoriesRecyclerView(view);
            });
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Popular");
    }

    private void setupCategoriesRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.popular_categories_rv__fragment_popular_items);

        // Создание адаптера и установка данных
        CategoriesAdapter adapter = new CategoriesAdapter(popularCategoriesList, categoryDetailsClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new PopularItemSpacingDecoration(DpToPixels.convert(12, requireActivity()), DpToPixels.convert(26, requireActivity())));
    }

    private void setupPopularItemsRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.popular_items_rv__fragment_popular_items);

        // Создание адаптера и установка данных
        PopularItemsGridAdapter adapter = new PopularItemsGridAdapter(popularProductsList, requireActivity(), productDetailsClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new PopularItemGridSpacingDecoration(DpToPixels.convert(10, requireActivity()), DpToPixels.convert(20, requireActivity())));
    }
}