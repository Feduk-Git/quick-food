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

import com.example.quick_food.R;
import com.example.quick_food.adapters.CategoriesAdapter;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.models.CategoryModel;
import com.example.quick_food.utils.CategoryGridSpacingDecoration;
import com.example.quick_food.utils.DpToPixels;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {
    private List<CategoryModel> categoriesList;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentTitleChangeListener = (OnFragmentTitleChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnFragmentTitleChangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriesList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            categoriesList.add(new CategoryModel("Category " + i, ""));
        }
        setupCategoriesRecyclerView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Categories");
    }

    private void setupCategoriesRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.categories_rv__fragment_categories);

        // Создание адаптера и установка данных
        CategoriesAdapter adapter = new CategoriesAdapter(categoriesList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CategoryGridSpacingDecoration(DpToPixels.convert(12, getContext()), DpToPixels.convert(26, getContext())));
    }
}