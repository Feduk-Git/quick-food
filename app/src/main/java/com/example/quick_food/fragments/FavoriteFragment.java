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
import com.example.quick_food.adapters.FavoriteItemsAdapter;
import com.example.quick_food.adapters.SearchItemsAdapter;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.FavoriteListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.DishModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.SearchItemSpacingDecoration;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteItemsAdapter.OnFavoriteIsEmptyListener {
    private List<DishModel> itemsList;
    private RecyclerView recyclerView;
    private TextView favoriteIsEmptyTV;
    private CartListener cartListener;
    private OnProductDetailsClickListener productDetailsClickListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private FavoriteListener favoriteListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            favoriteListener = (FavoriteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement FavoriteListener");
        }
        try {
            cartListener = (CartListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement CartListener");
        }
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = rootView.findViewById(R.id.favorite_items_rv__favorite_fragment);
        favoriteIsEmptyTV = rootView.findViewById(R.id.empty_favorite_tv__favorite_fragment);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemsList = favoriteListener.getFavoriteList();

        if (itemsList == null || itemsList.isEmpty()) {
            favoriteIsEmptyVisibilitySet();
        }
        else {
            favoriteIsEmptyTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            setupSearchItemsRecyclerView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Favorite");
    }

    @Override
    public void onFavoriteIsEmpty() {
        favoriteIsEmptyVisibilitySet();
    }

    private void setupSearchItemsRecyclerView() {
        // Создание адаптера и установка данных
        FavoriteItemsAdapter adapter = new FavoriteItemsAdapter(itemsList, cartListener, favoriteListener, productDetailsClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SearchItemSpacingDecoration(DpToPixels.convert(12, getContext())));
    }

    public void favoriteIsEmptyVisibilitySet() {
        favoriteIsEmptyTV.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}