package com.leonino.vneocherediandroid.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.adapters.BasketAdapter;
import com.leonino.vneocherediandroid.enums.Constants;
import com.leonino.vneocherediandroid.models.BasketProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        BasketAdapter adapter = new BasketAdapter(this,
                loadProducts(preferences), findViewById(R.id.total_price_basket));
        RecyclerView basketList = findViewById(R.id.basket_list);

        basketList.setLayoutManager(layoutManager);
        basketList.setAdapter(adapter);

        setListeners(editor);
    }

    private void setListeners(SharedPreferences.Editor editor) {
        findViewById(R.id.basket_back).setOnClickListener(view -> finish());

        findViewById(R.id.clear_basket_button).setOnClickListener(view -> {
            editor.putStringSet(Constants.BASKET_PRODUCTS, new TreeSet<>());

            editor.commit();
        });

        findViewById(R.id.basket_go_map).setOnClickListener(view -> {
            TextView priceView = findViewById(R.id.total_price_basket);
            Intent intent = new Intent(this, MapActivity.class);

            intent.putExtra(Constants.TOTAL_PRICE, priceView.getText());

            startActivity(intent);
        });
    }

    private List<BasketProduct> loadProducts(SharedPreferences preferences) {
        List<BasketProduct> products = new ArrayList<>();

        Set<String> basketProducts =
                new TreeSet<>(preferences.getStringSet(Constants.BASKET_PRODUCTS, new TreeSet<>()));

        for(String s : basketProducts) {
            String[] productStringArr = s.split("&");

            BasketProduct product = new BasketProduct(Long.parseLong(productStringArr[0]),
                    productStringArr[1], productStringArr[2], productStringArr[3],
                    Integer.parseInt(productStringArr[5]));

            products.add(product);
        }

        return products;
    }
}