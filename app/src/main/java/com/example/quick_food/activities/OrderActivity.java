package com.example.quick_food.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quick_food.QuickFoodApplication;
import com.example.quick_food.R;
import com.example.quick_food.ServerConnection;
import com.example.quick_food.models.CartItem;
import com.example.quick_food.models.City;
import com.example.quick_food.models.Order;
import com.example.quick_food.models.OrderStatus;
import com.example.quick_food.models.SharedViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private List<CartItem> items;
    private List<City> cities;
    private float totalPrice;
    private TextInputEditText nameTIET;
    private TextInputEditText surnameTIET;
    private TextInputEditText addressTIET;
    private TextInputEditText phoneNumberTIET;
    private TextInputEditText descriptionTIET;
    private TextInputLayout nameTIL;
    private TextInputLayout surnameTIL;
    private TextInputLayout addressTIL;
    private TextInputLayout phoneNumberTIL;
    private TextInputLayout descriptionTIL;
    private TextInputLayout cityTIL;
    private AutoCompleteTextView cityACTV;
    private City selectedCity;
    private SharedViewModel sharedVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        sharedVM = ((QuickFoodApplication) getApplication()).getSharedViewModel();
        items = sharedVM.getCartItems();

        nameTIET = findViewById(R.id.name_tiet__activity_order);
        surnameTIET = findViewById(R.id.surname_tiet__activity_order);
        addressTIET = findViewById(R.id.address_tiet__activity_order);
        phoneNumberTIET = findViewById(R.id.phone_number_tiet__activity_order);
        descriptionTIET = findViewById(R.id.description_tiet__activity_order);

        nameTIL = findViewById(R.id.name_til__activity_order);
        surnameTIL = findViewById(R.id.surname_til__activity_order);
        addressTIL = findViewById(R.id.address_til__activity_order);
        phoneNumberTIL = findViewById(R.id.phone_number_til__activity_order);
        descriptionTIL = findViewById(R.id.description__til__activity_order);
        cityTIL = findViewById(R.id.city_til__activity_order);

        cityACTV = findViewById(R.id.city_actv__activity_order);

        setupPriceTV();
        setupOnEditTextFocusListeners();
        setupConfirmOrderBt();
        setupOnBackImageViewClickListener();

        setupCityACTV();
    }

    private void setupCityACTV() {
        new Thread(() -> {
            cities = ServerConnection.getInstance().getCities();

            runOnUiThread(() -> {
                ArrayAdapter<City> adapter = new ArrayAdapter<>(this, R.layout.city_item, cities);
                cityACTV.setAdapter(adapter);
                cityACTV.setOnFocusChangeListener((v, hasFocus) -> {
                    cityACTV.showDropDown();
                });
                cityACTV.setOnItemClickListener((parent, view, position, id) -> {
                    selectedCity = (City) parent.getItemAtPosition(position);
                });
            });
        }).start();
    }

    private void setupOnEditTextFocusListeners() {
        nameTIET.setOnFocusChangeListener((v, hasFocus) -> onEditTextFocusChanges(nameTIL, hasFocus));
        surnameTIET.setOnFocusChangeListener((v, hasFocus) -> onEditTextFocusChanges(surnameTIL, hasFocus));
        addressTIET.setOnFocusChangeListener((v, hasFocus) -> onEditTextFocusChanges(addressTIL, hasFocus));
        phoneNumberTIET.setOnFocusChangeListener((v, hasFocus) -> onEditTextFocusChanges(phoneNumberTIL, hasFocus));
        descriptionTIET.setOnFocusChangeListener((v, hasFocus) -> onEditTextFocusChanges(descriptionTIL, hasFocus));
    }

    private void onEditTextFocusChanges(TextInputLayout view, boolean hasFocus) {
        int accentColor = AppCompatResources.getColorStateList(this, R.color.accent).getDefaultColor();
        int greyColor = AppCompatResources.getColorStateList(this, R.color.grey).getDefaultColor();

        if (hasFocus) {
            view.setCounterTextColor(ColorStateList.valueOf(accentColor));
            view.setHelperTextEnabled(true);
        }
        else {
            view.setCounterTextColor(ColorStateList.valueOf(greyColor));
            view.setHelperTextEnabled(false);
        }

    }

    private void setupPriceTV() {
        TextView priceTV = findViewById(R.id.price_tv__activity_order);

        for (CartItem cartItem: items) {
            totalPrice += cartItem.getTotalPrice();
        }
        priceTV.setText(String.valueOf(Math.round(totalPrice * 100.0) / 100.0));
    }

    private void setupConfirmOrderBt() {
        Button confirmOrderBt = findViewById(R.id.confirm_order_bt__activity_order);

        confirmOrderBt.setOnClickListener(v -> {
            String name = nameTIET.getText().toString();
            String surname = surnameTIET.getText().toString();
            String address = addressTIET.getText().toString();
            String phoneNumber = phoneNumberTIET.getText().toString();
            String selectedCityName = cityACTV.getText().toString();

            boolean inputDataIsOK = true;

            if (name.isEmpty()) {
                nameTIL.setHelperTextEnabled(true);
                nameTIL.setHelperText("Required*");
                inputDataIsOK = false;
            }
            else
                nameTIL.setHelperTextEnabled(false);

            if (surname.isEmpty()) {
                surnameTIL.setHelperTextEnabled(true);
                surnameTIL.setHelperText("Required*");
                inputDataIsOK = false;
            }
            else
                surnameTIL.setHelperTextEnabled(false);

            if (address.isEmpty()) {
                addressTIL.setHelperTextEnabled(true);
                addressTIL.setHelperText("Required*");
                inputDataIsOK = false;
            }
            else
                addressTIL.setHelperTextEnabled(false);

            if (phoneNumber.isEmpty() || phoneNumber.length() < 9) {
                phoneNumberTIL.setHelperTextEnabled(true);
                phoneNumberTIL.setHelperText("Required 9 numbers*");
                inputDataIsOK = false;
            }
            else
                phoneNumberTIL.setHelperTextEnabled(false);

            if (selectedCity == null || !selectedCity.name.equals(selectedCityName)) {
                cityTIL.setHelperTextEnabled(true);
                cityTIL.setHelperText("Select a city from list*");
                inputDataIsOK = false;
            }
            else
                cityTIL.setHelperTextEnabled(false);

            if (inputDataIsOK) {
                Order order = new Order();
                order.name = name;
                order.surname = surname;
                order.address = address;
                order.phoneNumber = phoneNumber;
                order.description = descriptionTIET.getText().toString();
                order.cartItems = items;
                order.price = totalPrice;
                order.status = OrderStatus.WAITING_CONFIRM;
                order.city = selectedCity;

                AlertDialog.Builder successfulOrderAlert = new AlertDialog.Builder(this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_view, null);
                successfulOrderAlert.setView(dialogView);
                Button okBt = dialogView.findViewById(R.id.ok_bt__dialog_view);

                AlertDialog alertDialog = successfulOrderAlert.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                okBt.setOnClickListener(successfulOrderDialog -> {
                    sharedVM.confirmOrder(order);
                    alertDialog.dismiss();
                    finish();
                });

                alertDialog.show();
            }
        });
    }

    private void setupOnBackImageViewClickListener() {
        ImageView iv = findViewById(R.id.back_button_iv__activity_order);
        iv.setOnClickListener(v -> finish());
    }
}