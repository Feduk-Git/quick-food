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
import com.example.quick_food.adapters.OrderDetailsDishesListAdapter;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.models.Order;
import com.example.quick_food.models.SharedViewModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.SearchItemSpacingDecoration;

public class OrderDetailsFragment extends Fragment {
    private Order currentOrder;
    private SharedViewModel sharedVM;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private OnProductDetailsClickListener productDetailsClickListener;
    private RecyclerView rv;
    private TextView nameTV;
    private TextView addressTV;
    private TextView phoneTV;
    private TextView dateTimeTV;
    private TextView statusTV;
    private TextView descriptionTV;
    private TextView priceTV;

    public OrderDetailsFragment(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentTitleChangeListener = (OnFragmentTitleChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnFragmentTitleChangeListener");
        }
        try {
            productDetailsClickListener = (OnProductDetailsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnProductDetailsClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedVM = ((QuickFoodApplication)requireActivity().getApplication()).getSharedViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_details, container, false);
        rv = rootView.findViewById(R.id.dishes_list_rv__order_details_fragment);
        nameTV = rootView.findViewById(R.id.name_tv__order_details_fragment);
        addressTV = rootView.findViewById(R.id.address_tv__order_details_fragment);
        phoneTV = rootView.findViewById(R.id.phone_number_tv__order_details_fragment);
        dateTimeTV = rootView.findViewById(R.id.date_tv__order_details_fragment);
        statusTV = rootView.findViewById(R.id.status_tv__order_details_fragment);
        descriptionTV = rootView.findViewById(R.id.description_tv__order_details_fragment);
        priceTV = rootView.findViewById(R.id.price_tv__order_details_fragment);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupRecyclerView();

        nameTV.setText(currentOrder.name + " " + currentOrder.surname);
        addressTV.setText(currentOrder.address);
        phoneTV.setText("+380" + currentOrder.phoneNumber);
        dateTimeTV.setText(currentOrder.dateTime);
        statusTV.setText(currentOrder.status.getDisplayValue());
        priceTV.setText(String.valueOf(Math.round(currentOrder.price * 100.0) / 100.0));

        if (currentOrder.description.isEmpty())
            descriptionTV.setVisibility(View.GONE);
        else {
            descriptionTV.setVisibility(View.VISIBLE);
            descriptionTV.setText(currentOrder.description);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Order details");
    }

    private void setupRecyclerView() {
        // Создание адаптера и установка данных
        OrderDetailsDishesListAdapter adapter = new OrderDetailsDishesListAdapter(currentOrder.cartItems, productDetailsClickListener);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SearchItemSpacingDecoration(DpToPixels.convert(12, requireActivity())));
    }
}