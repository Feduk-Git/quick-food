package com.example.quick_food.models;

import androidx.lifecycle.ViewModel;

import com.example.quick_food.ServerConnection;
import com.example.quick_food.interfaces.OrderListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private List<CartItem> cartItems;
    private List<Product> favoriteItems;
    private LinkedList<Order> orders;
    private OrderListener orderListener;

//region [Cart]
    public boolean addToCart(Product item) {
        if (cartItems == null)
            cartItems = new ArrayList<>();

        if (!checkCartItemExists(item)) {
            cartItems.add(new CartItem(item));
            return true;
        } else
            return false;
    }

    public boolean removeFromCart(Product item) {
        CartItem itemToRemove = null;

        if (cartItems == null)
            return false;

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().id == item.id)
                itemToRemove = cartItem;
        }

        if (itemToRemove != null) {
            cartItems.remove(itemToRemove);
            return true;
        } else
            return false;
    }

    public List<CartItem> getCartItems() {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    public boolean checkCartItemExists(Product item) {
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                if (cartItem.getProduct().id == item.id)
                    return true;
            }
        }

        return false;
    }

    public void loadCartItems(List<CartItem> loadedCartItems) {
        cartItems = loadedCartItems;
    }
//endregion

//region [Favorite]
    public boolean addToFavorite(Product item) {
        if (favoriteItems == null)
            favoriteItems = new ArrayList<>();

        if (!checkFavoriteItemExists(item)) {
            favoriteItems.add(item);
            return true;
        } else
            return false;
    }

    public boolean removeFromFavorite(Product item) {
        Product itemToRemove = null;

        if (favoriteItems == null)
            return false;

        for (Product favoriteItem : favoriteItems) {
            if (favoriteItem.id == item.id)
                itemToRemove = favoriteItem;
        }

        if (itemToRemove != null) {
            favoriteItems.remove(itemToRemove);
            return true;
        } else
            return false;
    }

    public List<Product> getFavoriteList() {
        if (favoriteItems == null) {
            favoriteItems = new ArrayList<>();
        }
        return favoriteItems;
    }

    public boolean checkFavoriteItemExists(Product item) {
        if (favoriteItems != null) {
            for (Product favoriteItem : favoriteItems) {
                if (favoriteItem.id == item.id)
                    return true;
            }
        }

        return false;
    }

    public void loadFavoriteItems(List<Product> loadedFavoriteItems) {
        favoriteItems = loadedFavoriteItems;
    }
//endregion

    public void addOrder(Order order) {
        if (orders == null)
            orders = new LinkedList<>();

        orders.addFirst(order);
        cartItems = new ArrayList<>();
    }

    public void confirmOrder(Order order) {
        orderListener.confirmOrder(order);
    }

    public void loadOrdersList(LinkedList<Order> loadedOrdersList) {
        orders = loadedOrdersList;
    }

    public List<Order> getOrders() {
        if (orders == null) {
            orders = new LinkedList<>();
        }
        else {
            Thread thread = new Thread(() -> {
                List<Order> ordersToDelete = new ArrayList<>();
                for (int i = 0; i < orders.size(); i++) {
                    Order updatedOrder = ServerConnection.getInstance().getCurrentOrderStateById(orders.get(i).id);
                    if (updatedOrder == null)
                        ordersToDelete.add(orders.get(i));
                    else
                        orders.set(i, updatedOrder);
                }

                for (Order order : ordersToDelete) {
                    orders.remove(order);
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    public void setOrderListener(OrderListener orderListener) {
        this.orderListener = orderListener;
    }
}
