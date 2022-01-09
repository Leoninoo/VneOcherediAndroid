package com.leonino.vneocherediandroid.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.enums.Constants;
import com.leonino.vneocherediandroid.models.BasketProduct;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketHolder> {
    private final Context context;
    private final List<BasketProduct> products;
    private final TextView totalPriceView;

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public BasketAdapter(Context context, List<BasketProduct> products, TextView totalPriceView) {
        this.context = context;
        this.products = products;
        this.totalPriceView = totalPriceView;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    @NonNull
    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_basket, parent, false);
        return new BasketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder holder, int position) {
        BasketProduct product = products.get(position);

        if(!product.getImage().isEmpty())
            Picasso.with(context)
                .load(product.getImage())
                .into(holder.image);

        holder.name.setText(product.getName());

        float price =
                Float.parseFloat(product.getPrice().split(" ")[0].replace(",", "."));
        price *= product.getCounter();
        String s = (price + " руб").replace(".", ",");

        holder.price.setText(s);

        String str = product.getCounter() + " шт";
        holder.counter.setText(str);

        if(position == products.size() - 1)
            holder.line.setVisibility(View.INVISIBLE);

        checkTotalPrice();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    protected final static class BasketHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView name;
        private final TextView price;
        private final TextView counter;
        private final ImageView line;

        public BasketHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_product_basket);
            name = itemView.findViewById(R.id.name_product_basket);
            price = itemView.findViewById(R.id.price_product_basket);
            counter = itemView.findViewById(R.id.counter_product_basket);
            line = itemView.findViewById(R.id.basket_line);
        }
    }

    private void checkTotalPrice() {
        Set<String> basketProducts = preferences.getStringSet(Constants.BASKET_PRODUCTS, new TreeSet<>());
        int totalPrice = 0;

        for(String s : basketProducts) {
            String[] product = s.split("&");

            float productPrice =
                    Float.parseFloat(product[3].split(" ")[0].replace(",", "."));

            totalPrice += productPrice * Integer.parseInt(product[5]);
        }


        String str = totalPrice + " руб";


        totalPriceView.setText(str);
    }
}
