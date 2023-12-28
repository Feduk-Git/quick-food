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
import android.widget.Button;
import android.widget.TextView;

import com.example.quick_food.QuickFoodApplication;
import com.example.quick_food.R;
import com.example.quick_food.adapters.CartItemsAdapter;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.interfaces.OnProductDetailsClickListener;
import com.example.quick_food.interfaces.OrderListener;
import com.example.quick_food.models.CartItem;
import com.example.quick_food.models.SharedViewModel;
import com.example.quick_food.utils.DpToPixels;
import com.example.quick_food.utils.SearchItemSpacingDecoration;

import java.util.List;

public class CartFragment extends Fragment implements CartItemsAdapter.OnCountChangeClickListener, CartItemsAdapter.OnItemRemoveListener, CartItemsAdapter.OnCartIsEmptyListener {
    private List<CartItem> cartItems;
    private TextView totalPriceTV;
    private RecyclerView rv;
    private ConstraintLayout emptyCartCL;
    private ConstraintLayout bottomCL;
    private CartListener cartListener;
    private OrderListener orderListener;
    private CartItemsAdapter.OnCountChangeClickListener countChangeClickListenerListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private OnProductDetailsClickListener productDetailsClickListener;
    private double totalPrice;
    private SharedViewModel sharedVM;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            countChangeClickListenerListener = (CartItemsAdapter.OnCountChangeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement CartItemsAdapter.OnCountChangeClickListener");
        }
        try {
            orderListener = (OrderListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OrderListener");
        }
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
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        totalPriceTV = rootView.findViewById(R.id.price_tv__cart_fragment);
        emptyCartCL = rootView.findViewById(R.id.empty_cart_cl__cart_fragment);
        bottomCL = rootView.findViewById(R.id.bottom_constraint__cart_fragment);
        rv = rootView.findViewById(R.id.cart_items_rv__cart_fragment);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cartItems = sharedVM.getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            cartIsEmptyVisibilitySet();
        }
        else {
            totalPrice = 0;
            emptyCartCL.setVisibility(View.GONE);
            bottomCL.setVisibility(View.VISIBLE);
            rv.setVisibility(View.VISIBLE);
            for (CartItem cartItem: cartItems) {
                totalPrice += cartItem.getTotalPrice();
            }
            totalPriceTV.setText(String.valueOf(Math.round(totalPrice * 100.0) / 100.0));

            setupRecyclerView();

            Button orderNowBt = view.findViewById(R.id.order_now_bt__cart_fragment);
            orderNowBt.setOnClickListener(v -> orderListener.orderConfirmation());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Cart");
        cartItems = sharedVM.getCartItems();
        if (cartItems == null || cartItems.isEmpty()) {
            cartIsEmptyVisibilitySet();
        }
    }

    public void setupRecyclerView() {
        // Создание адаптера и установка данных
        CartItemsAdapter adapter = new CartItemsAdapter(cartItems,  cartListener, this, this, productDetailsClickListener);
        adapter.addCountChangeClickListener(this);
        adapter.addCountChangeClickListener(countChangeClickListenerListener);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SearchItemSpacingDecoration(DpToPixels.convert(12, requireActivity())));
    }

    @Override
    public void onIncreaseCountClicked(int position) {
        totalPrice += cartItems.get(position).getProduct().price;
        totalPriceTV.setText(String.valueOf(Math.round(totalPrice * 100.0) / 100.0));
    }

    @Override
    public void onDecreaseCountClicked(int position) {
        totalPrice -= cartItems.get(position).getProduct().price;
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