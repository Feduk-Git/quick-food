package com.example.quick_food.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quick_food.R;
import com.example.quick_food.adapters.CartItemsAdapter;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.models.CartItemModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.SearchItemSpacingDecoration;

import java.util.List;

public class CartFragment extends Fragment implements CartItemsAdapter.OnCountChangeClickListener, CartItemsAdapter.OnItemRemoveListener, CartItemsAdapter.OnCartIsEmptyListener {
    private View rootView;
    private List<CartItemModel> cartItems;
    private TextView totalPriceTV;
    private RecyclerView rv;
    private ConstraintLayout emptyCartCL;
    private ConstraintLayout bottomCL;
    private CartListener cartListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private double totalPrice = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            cartListener = (CartListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement CartListener");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        totalPriceTV = rootView.findViewById(R.id.price_tv__cart_fragment);
        emptyCartCL = rootView.findViewById(R.id.empty_cart_cl__cart_fragment);
        bottomCL = rootView.findViewById(R.id.bottom_constraint__cart_fragment);
        rv = rootView.findViewById(R.id.cart_items_rv__cart_fragment);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cartItems = cartListener.getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            cartIsEmptyVisibilitySet();
        }
        else {
            emptyCartCL.setVisibility(View.GONE);
            bottomCL.setVisibility(View.VISIBLE);
            rv.setVisibility(View.VISIBLE);
            for (CartItemModel cartItem: cartItems) {
                totalPrice += cartItem.getItem().price;
            }
            totalPriceTV.setText(String.valueOf(Math.round(totalPrice * 100.0) / 100.0));

            setupRecyclerView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Cart");
    }

    public void setupRecyclerView() {
        // Создание адаптера и установка данных
        CartItemsAdapter adapter = new CartItemsAdapter(cartItems, this, cartListener, this, this);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SearchItemSpacingDecoration(DpToPixels.convert(12, getContext())));
    }

    @Override
    public void onIncreaseCountClicked(int position) {
        totalPrice += cartItems.get(position).getItem().price;
        totalPriceTV.setText(String.valueOf(Math.round(totalPrice * 100.0) / 100.0));
    }

    @Override
    public void onDecreaseCountClicked(int position) {
        totalPrice -= cartItems.get(position).getItem().price;
        totalPriceTV.setText(String.valueOf(Math.round(totalPrice * 100.0) / 100.0));
    }

    @Override
    public void onItemRemoved(double itemPrice) {
        totalPrice -= itemPrice;
        totalPriceTV.setText(String.valueOf(Math.round(totalPrice * 100.0) / 100.0));
    }

    @Override
    public void onCartIsEmpty() {
        cartIsEmptyVisibilitySet();
    }

    public void cartIsEmptyVisibilitySet() {
        emptyCartCL.setVisibility(View.VISIBLE);
        bottomCL.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);
    }
}