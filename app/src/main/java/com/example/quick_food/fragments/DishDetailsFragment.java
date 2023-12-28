package com.example.quick_food.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quick_food.QuickFoodApplication;
import com.example.quick_food.R;
import com.example.quick_food.adapters.DishImgViewPagerAdapter;
import com.example.quick_food.interfaces.CartListener;
import com.example.quick_food.interfaces.FavoriteListener;
import com.example.quick_food.interfaces.OnFragmentTitleChangeListener;
import com.example.quick_food.models.Product;
import com.example.quick_food.models.SharedViewModel;
import com.example.quick_food.utils.DpToPixels;

import java.util.ArrayList;
import java.util.List;

public class DishDetailsFragment extends Fragment {
    private LinearLayout ll;
    private List<String> imgList;
    private Product item;
    private CartListener cartListener;
    private OnFragmentTitleChangeListener fragmentTitleChangeListener;
    private FavoriteListener favoriteListener;
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
            fragmentTitleChangeListener = (OnFragmentTitleChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnFragmentTitleChangeListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedVM = ((QuickFoodApplication)requireActivity().getApplication()).getSharedViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dish_details, container, false);

        ll = rootView.findViewById(R.id.dots_layout__dish_details_fragment);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nameTV = view.findViewById(R.id.dish_name__dish_details_fragment);
        TextView descTV = view.findViewById(R.id.dish_desc__dish_details_fragment);
        TextView priceTV = view.findViewById(R.id.price_tv__dish_details_fragment);
        Button addToCartBT = view.findViewById(R.id.add_to_cart_bt__dish_details_fragment);
        ImageView favoriteButtonIV = view.findViewById(R.id.favorite_bt_iv__dish_details_fragment);

        nameTV.setText(item.name);
        descTV.setText(item.description);
        priceTV.setText(String.valueOf(item.price));

        if (sharedVM.checkCartItemExists(item))
            addToCartBT.setText("Remove from cart");
        else
            addToCartBT.setText("Add to cart");

        addToCartBT.setOnClickListener(v -> {
            if (cartListener.removeFromCart(item)) {
                addToCartBT.setText("Add to cart");
            }
            else if (cartListener.addToCart(item)) {
                addToCartBT.setText("Remove from cart");
            }
        });

        setupViewPager(view);
        setupDots(imgList.size(), view);

        if (sharedVM.checkFavoriteItemExists(item))
            favoriteButtonIV.setImageResource(R.drawable.ic_heart_filled);
        else
            favoriteButtonIV.setImageResource(R.drawable.ic_heart);

        favoriteButtonIV.setOnClickListener(v -> {
            if (favoriteListener.removeFromFavorite(item))
                favoriteButtonIV.setImageResource(R.drawable.ic_heart);
            else if (favoriteListener.addToFavorite(item))
                favoriteButtonIV.setImageResource(R.drawable.ic_heart_filled);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTitleChangeListener.onTitleChanged("Details");
    }

    public void setDishModel(Product item) {
        this.item = item;
    }

    private void setupViewPager(View view) {
        imgList = new ArrayList<>();
        imgList.add("");

        DishImgViewPagerAdapter adapter = new DishImgViewPagerAdapter(item.id, imgList);
        ViewPager2 viewPager2 = view.findViewById(R.id.dish_img_vp__dish_details_fragment);

        viewPager2.setAdapter(adapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                updateTabLayout(position, view.getContext());
            }
        });
    }

    private void updateTabLayout(int currentItem, Context context) {
        for (int i = 0; i < ll.getChildCount(); i++) {
            if (i == currentItem) {
                ll.getChildAt(i).setBackgroundResource(R.drawable.dot_active);
                ll.getChildAt(i).setLayoutParams(getParamsForDot(7,7, context));
            }
            else {
                ll.getChildAt(i).setBackgroundResource(R.drawable.dot_inactive);
                ll.getChildAt(i).setLayoutParams(getParamsForDot(5,5, context));
            }
        }
    }

    private void setupDots(int itemsCount, View v) {
        for (int i = 0; i < itemsCount; i++) {
            View view = new View(v.getContext());
            LinearLayout.LayoutParams params;
            if (i == 0)
                params = getParamsForDot(7,7, v.getContext());
            else
                params = getParamsForDot(5,5, v.getContext());

            view.setLayoutParams(params);
            ll.addView(view);
        }
    }

    private LinearLayout.LayoutParams getParamsForDot(int widthInDp, int heightInDp, Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DpToPixels.convert(widthInDp, context), DpToPixels.convert(heightInDp, context));
        params.setMargins(0,0,DpToPixels.convert(7, context),0);
        return params;
    }
}