package com.example.quick_food;

import com.example.quick_food.models.CartItem;
import com.example.quick_food.models.Category;
import com.example.quick_food.models.City;
import com.example.quick_food.models.Order;
import com.example.quick_food.models.Product;
import com.example.quick_food.models.ProductCount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerConnection {
    public final String host = "http://192.168.0.102:36985/";
    private static final ServerConnection connection = new ServerConnection();

    private ServerConnection () {

    }

    public static ServerConnection getInstance() {
        return connection;
    }

    public List<Product> getPopularProducts(int productsCount) {
        HttpURLConnection connection = null;
        List<Product> popularProducts = null;

        try {
            connection = (HttpURLConnection) (new URL(host + "api/product/get_popular_products/" + productsCount).openConnection());
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            String json = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            popularProducts = objectMapper.readValue(json, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return popularProducts;
    }

    public List<Category> getPopularCategories(int categoriesCount) {
        HttpURLConnection connection = null;
        List<Category> popularCategories = null;

        try {
            connection = (HttpURLConnection) (new URL(host + "api/category/get_popular_categories/" + categoriesCount).openConnection());
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            String json = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            popularCategories = objectMapper.readValue(json, new TypeReference<List<Category>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return popularCategories;
    }

    public List<Category> getCategories() {
        HttpURLConnection connection = null;
        List<Category> categories = null;

        try {
            connection = (HttpURLConnection) (new URL(host + "api/category/get_categories").openConnection());
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            String json = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            categories = objectMapper.readValue(json, new TypeReference<List<Category>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public List<Product> getProductsSearch(String searchString) {
        HttpURLConnection connection;
        List<Product> products = null;

        try {
            connection = (HttpURLConnection) (new URL(host + "api/product/get_products_search/" + searchString).openConnection());
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            String json = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            products = objectMapper.readValue(json, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<City> getCities() {
        HttpURLConnection connection;
        List<City> cities = null;

        try {
            connection = (HttpURLConnection) (new URL(host + "api/city/get_cities").openConnection());
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            String json = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            cities = objectMapper.readValue(json, new TypeReference<List<City>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public Order createNewOrder(Order order) {
        HttpURLConnection connection;
        List<ProductCount> productCounts = new ArrayList<>();

        for (CartItem cartItem: order.cartItems) {
            productCounts.add(new ProductCount(cartItem.getProduct().id, cartItem.getItemsCount()));
        }

        try {
            connection = (HttpURLConnection) (new URL(host + "api/order/create_new_order").openConnection());
            // Установка метода запроса и свойств
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Создание объекта ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            // Создание объекта ObjectNode
            ObjectNode jsonNode = objectMapper.createObjectNode();
            // Добавление свойств в JSON-объект
            jsonNode.put("name", order.name);
            jsonNode.put("surname", order.surname);
            jsonNode.put("address", order.address);
            jsonNode.put("phoneNumber", order.phoneNumber);
            jsonNode.put("description", order.description);
            jsonNode.put("productsCount", objectMapper.writeValueAsString(productCounts));
            jsonNode.put("cityId", order.city.id);
            String requestBody = jsonNode.toString();
            connection.setRequestProperty("Content-Type", "application/json");

            // Запись requestBody в OutputStream
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseStringBuilder.append(line);
                }
                reader.close();
                inputStream.close();

                // Преобразовать JSON-строку в объект Order
                ObjectMapper responseMapper = new ObjectMapper();
                return responseMapper.readValue(responseStringBuilder.toString(), Order.class);
            }
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Order getCurrentOrderStateById(int orderId) {
        HttpURLConnection connection;
        Order order;

        try {
            connection = (HttpURLConnection) (new URL(host + "api/order/get_order_state_by_id/" + orderId).openConnection());
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            String json = response.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            order = objectMapper.readValue(json, new TypeReference<Order>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return order;
    }
}
