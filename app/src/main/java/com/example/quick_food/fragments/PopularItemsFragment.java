package com.example.quick_food.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quick_food.R;
import com.example.quick_food.activities.MainActivity;
import com.example.quick_food.adapters.CategoriesAdapter;
import com.example.quick_food.adapters.PopularItemsGridAdapter;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.CategoryModel;
import com.example.quick_food.models.DishModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.PopularItemGridSpacingDecoration;
import com.example.quick_food.utils.PopularItemSpacingDecoration;

import java.util.ArrayList;
import java.util.List;

public class PopularItemsFragment extends Fragment {
    private List<CategoryModel> popularCategoriesList;
    private List<DishModel> itemsList;
    private OnProductDetailsClickListener productDetailsClickListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
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

        popularCategoriesList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            popularCategoriesList.add(new CategoryModel("Category" + (i + 1), ""));
        }

        itemsList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            itemsList.add(new DishModel("", "item" + (i + 1), "testDesc"));
        }

        setupCategoriesRecyclerView(view);
        setupItemsRecyclerView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Popular");
    }

    private void setupCategoriesRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.popular_categories_rv__fragment_popular_items);

        // Создание адаптера и установка данных
        CategoriesAdapter adapter = new CategoriesAdapter(popularCategoriesList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new PopularItemSpacingDecoration(DpToPixels.convert(12, getContext()), DpToPixels.convert(26, getContext())));
    }

    private void setupItemsRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.popular_items_rv__fragment_popular_items);

        // Создание адаптера и установка данных
        PopularItemsGridAdapter adapter = new PopularItemsGridAdapter(itemsList, getContext(), productDetailsClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new PopularItemGridSpacingDecoration(DpToPixels.convert(10, getContext()), DpToPixels.convert(20, getContext())));
    }
}