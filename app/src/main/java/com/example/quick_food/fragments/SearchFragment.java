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
import com.example.quick_food.activities.MainActivity;
import com.example.quick_food.adapters.SearchItemsAdapter;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.DishModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.SearchItemSpacingDecoration;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private List<DishModel> itemsList;
    private CartListener cartListener;
    private OnProductDetailsClickListener productDetailsClickListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private String searchString;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemsList = new ArrayList<>();
        itemsList.add(new DishModel("", "Margherita Regular 12x12”", "Get extra toppings free"));
        itemsList.add(new DishModel("", "House Special Pizza", "Sausage, Mushrooms, Olives, Pepperoni, Green Peppers, Onions"));
        itemsList.add(new DishModel("", "Vegetarian Pizza", "Broccoli, Mushrooms, Olives, Green Peppers"));

        for (int i = 0; i < 7; i++) {
            itemsList.add(new DishModel("", "item " + i, "desc " + i));
        }

        TextView countFoundItemsTV = view.findViewById(R.id.count_found_items_tv__search_fragment);
        countFoundItemsTV.setText("Found " + itemsList.size() + " items for \"" + searchString + "\"");
        setupSearchItemsRecyclerView(view);
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
        RecyclerView recyclerView = view.findViewById(R.id.found_items_rv__search_fragment);

        // Создание адаптера и установка данных
        SearchItemsAdapter adapter = new SearchItemsAdapter(itemsList, (MainActivity)getActivity(), cartListener, productDetailsClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SearchItemSpacingDecoration(DpToPixels.convert(12, getContext())));
    }
}