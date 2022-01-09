package com.leonino.vneocherediandroid.service;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.leonino.vneocherediandroid.enums.Category;
import com.leonino.vneocherediandroid.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ProductService {
    private String productsString;
    private final Category category;

    public ProductService(Category category) {
        this.category = category;
    }

    public List<Product> productsFactory() {
        List<Product> resultProducts = new ArrayList<>();

        getHttpResponse();

        while (productsString == null) {}

        try {
            JSONArray rootJSON = new JSONArray(new JSONTokener(productsString));
            for (int i = 0; i < rootJSON.length(); i++) {
                JSONObject o = rootJSON.getJSONObject(i);
                Product product = new Gson().fromJson(String.valueOf(o), Product.class);
                resultProducts.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultProducts;
    }

    private void getHttpResponse() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("https://vne-ocheredi.herokuapp.com/products?category=" + category)
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            // если запрос неудачный
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e.getMessage());
            }

            // В случае успешного запроса
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // в result выводится полученный текст от сервера
                assert response.body() != null;
                productsString = response.body().string();
            }
        });
    }
}
