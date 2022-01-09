package com.leonino.vneocherediandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.adapters.CatalogAdapter;
import com.leonino.vneocherediandroid.adapters.CategoryAdapter;
import com.leonino.vneocherediandroid.enums.Category;
import com.leonino.vneocherediandroid.models.CategoryItem;
import com.leonino.vneocherediandroid.models.Product;
import com.leonino.vneocherediandroid.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    private List<CategoryItem> categoryItems;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        categoryItems = getCategoryItems();

        String category = getIntent().getStringExtra("category");

        int position = setChosenCategory(category);

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryItems, true, findViewById(R.id.catalog_header));
        RecyclerView.LayoutManager categoryLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView categoryRecyclerView = findViewById(R.id.catalog_categories);

        categoryRecyclerView.setLayoutManager(categoryLayoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);
        if (position > 0)
            categoryRecyclerView.scrollToPosition(position);

//        setCategory(category);
//
//        List<Product> products = productService.productsFactory();
//
//        CatalogAdapter productAdapter = new CatalogAdapter(this, products, findViewById(R.id.total_price_catalog));
//        RecyclerView.LayoutManager catalogLayoutManager = new GridLayoutManager(this, 2);
//
//        RecyclerView catalogRecyclerView = findViewById(R.id.catalog_products);
//
//        catalogRecyclerView.setLayoutManager(catalogLayoutManager);
//        catalogRecyclerView.setAdapter(productAdapter);
    }

    private int setChosenCategory(String category) {
        for(int i = 0; i < categoryItems.size(); i++) {
            if(categoryItems.get(i).getName().equals(category)) {
                categoryItems.get(i).setChosen(true);
                return i;
            }
        }

        return 0;
    }

    private List<CategoryItem> getCategoryItems() {
        List<CategoryItem> items = new ArrayList<>();

        items.add(new CategoryItem(R.drawable.ic_vegetables, "Овощи"));
        items.add(new CategoryItem(R.drawable.ic_milk, "Молоко"));
        items.add(new CategoryItem(R.drawable.ic_meat, "Мясо"));
        items.add(new CategoryItem(R.drawable.ic_fish, "Рыба"));

        items.add(new CategoryItem(R.drawable.ic_fish, "Рыба"));
        items.add(new CategoryItem(R.drawable.ic_fish, "Рыба"));
        items.add(new CategoryItem(R.drawable.ic_fish, "Рыба"));

        return items;
    }

    private void setCategory(String category) {
        switch (category) {
            case "Овощи":
                productService = new ProductService(Category.vegetables);
                break;

            case "Молоко":
                productService = new ProductService(Category.milk);
                break;

            case "Мясо":
                productService = new ProductService(Category.meat);
                break;

            case "Рыба":
                productService = new ProductService(Category.fish);
                break;


            default: productService = new ProductService(Category.discounts);
        }
    }

    public void goBasket(View view) {
        Intent intent = new Intent(this, BasketActivity.class);

        startActivity(intent);
    }
}