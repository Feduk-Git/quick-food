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

import com.example.quick_food.QuickFoodApplication;
import com.example.quick_food.R;
import com.example.quick_food.ServerConnection;
import com.example.quick_food.adapters.SearchItemsAdapter;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.FavoriteListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.Product;
import com.example.quick_food.models.SharedViewModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.SearchItemSpacingDecoration;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private List<Product> itemsList;
    private RecyclerView recyclerView;
    private CartListener cartListener;
    private OnProductDetailsClickListener productDetailsClickListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private FavoriteListener favoriteListener;
    private String searchString;
    private SharedViewModel sharedVM;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedVM = ((QuickFoodApplication)requireActivity().getApplication()).getSharedViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = rootView.findViewById(R.id.found_items_rv__search_fragment);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView countFoundItemsTV = view.findViewById(R.id.count_found_items_tv__search_fragment);

        new Thread(() -> {
            itemsList = ServerConnection.getInstance().getProductsSearch(searchString);
            getActivity().runOnUiThread(() -> {
                countFoundItemsTV.setText("Found " + itemsList.size() + " items for \"" + searchString + "\"");
                setupSearchItemsRecyclerView(view);
            });
        }).start();


    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Search");
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    private void setupSearchItemsRecyclerView(View view) {
        // Создание адаптера и установка данных
        SearchItemsAdapter adapter = new SearchItemsAdapter(itemsList, cartListener, favoriteListener, productDetailsClickListener, sharedVM);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SearchItemSpacingDecoration(DpToPixels.convert(12, requireActivity())));
    }
}