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
import com.example.quick_food.adapters.OrdersAdapter;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnOrderDetailsClickListener;
import com.example.quick_food.models.Order;
import com.example.quick_food.models.SharedViewModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.SearchItemSpacingDecoration;

import java.util.List;

public class OrdersFragment extends Fragment {

    private SharedViewModel sharedVM;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private List<Order> orders;
    private RecyclerView rv;
    private TextView emptyOrdersListTV;
    private OnOrderDetailsClickListener onOrderDetailsClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentTitleChangeListener = (OnFragmentTitleChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnFragmentTitleChangeListener");
        }
        try {
            onOrderDetailsClickListener = (OnOrderDetailsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnOrderDetailsClickListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Orders list");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedVM = ((QuickFoodApplication)requireActivity().getApplication()).getSharedViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        rv = rootView.findViewById(R.id.orders_rv__orders_fragment);
        emptyOrdersListTV = rootView.findViewById(R.id.empty_orders_list_tv__orders_fragment);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        orders = sharedVM.getOrders();

        if (orders == null || orders.isEmpty()) {
            rv.setVisibility(View.GONE);
            emptyOrdersListTV.setVisibility(View.VISIBLE);
        }
        else {
            rv.setVisibility(View.VISIBLE);
            emptyOrdersListTV.setVisibility(View.GONE);
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        // Создание адаптера и установка данных
        OrdersAdapter adapter = new OrdersAdapter(orders, onOrderDetailsClickListener);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SearchItemSpacingDecoration(DpToPixels.convert(12, requireActivity())));
    }
}