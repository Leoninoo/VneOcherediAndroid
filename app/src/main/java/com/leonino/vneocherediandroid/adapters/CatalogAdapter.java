package com.leonino.vneocherediandroid.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.enums.Constants;
import com.leonino.vneocherediandroid.models.BasketProduct;
import com.leonino.vneocherediandroid.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ProductHolder> {
    private final List<Product> products;
    private final Context context;
    private final TextView totalPriceView;

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;


    public CatalogAdapter(Context context, List<Product> products, TextView totalPriceView) {
        this.products = products;
        this.context = context;
        this.totalPriceView = totalPriceView;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice());
        Picasso.with(context)
                .load(products.get(position).getImage())
                .into(holder.imageProduct);

        setListeners(holder, position);

        int counter = checkBasketProducts(products.get(position));

        if(counter > 0) {
            holder.counterBar.setVisibility(View.VISIBLE);
            holder.counterBox.setVisibility(View.VISIBLE);

            holder.addProductButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.addBasketText.setTextColor(context.getResources().getColor(R.color.black));

            String str = counter + " шт";
            holder.counter.setText(str);

            holder.counterBar.setMax(10);
            holder.counterBar.setProgress(counter);
            holder.counterBar.setOnSeekBarChangeListener(
                    onSeekBarChangeListener(holder, holder.addProductButton, position));
        }

        checkTotalPrice();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    protected static final class ProductHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView price;
        private final ImageView imageProduct;
        private final View addProductButton;
        private final View counterBox;
        private final SeekBar counterBar;
        private final TextView counter;
        private final TextView addBasketText;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_product_catalog);
            price = itemView.findViewById(R.id.price_product_catalog);
            imageProduct = itemView.findViewById(R.id.image_product_catalog);
            addProductButton = itemView.findViewById(R.id.add_product_button);
            counterBox = itemView.findViewById(R.id.counter_box);
            counterBar = itemView.findViewById(R.id.seek_product_bar);
            counter = itemView.findViewById(R.id.counter_product_basket);
            addBasketText = itemView.findViewById(R.id.go_basket_text);
        }
    }

    private void setListeners(ProductHolder holder, int position) {
        holder.addProductButton.setOnClickListener(view -> {
            view.setBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.addBasketText.setTextColor(context.getResources().getColor(R.color.black));

            holder.counterBox.setVisibility(View.VISIBLE);
            holder.counterBar.setVisibility(View.VISIBLE);

            holder.counter.setText("1 шт");

            Set<String> basketProducts =
                    new TreeSet<>(preferences.getStringSet(Constants.BASKET_PRODUCTS, new TreeSet<>()));

            String basketProduct = products.get(position).getId() + "&" +
                    products.get(position).getImage() + "&" + products.get(position).getName() + "&" +
                    products.get(position).getPrice() + "&" + products.get(position).getHref() + "&1";

            basketProducts.add(basketProduct);
            editor.putStringSet(Constants.BASKET_PRODUCTS, basketProducts);

            editor.apply();

            holder.counterBar.setMax(10);
            holder.counterBar.setProgress(1);
            holder.counterBar.setOnSeekBarChangeListener(onSeekBarChangeListener(holder, view, position));

            checkTotalPrice();
        });
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener(ProductHolder holder, View view, int position) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(seekBar.getVisibility() == View.INVISIBLE)
                    return;

                String str = i + " шт";
                holder.counter.setText(str);

                Set<String> basketProducts =
                        new TreeSet<>(preferences.getStringSet(Constants.BASKET_PRODUCTS, new TreeSet<>()));

                if (i == 0) {
                    view.setBackgroundColor(context.getResources().getColor(R.color.main_violet));
                    holder.addBasketText.setTextColor(context.getResources().getColor(R.color.white));

                    holder.counterBox.setVisibility(View.INVISIBLE);
                    holder.counterBar.setVisibility(View.INVISIBLE);
                }


                String basketProduct = products.get(position).getId() + "&" +
                        products.get(position).getImage() + "&" + products.get(position).getName() + "&" +
                        products.get(position).getPrice() + "&" + products.get(position).getHref() + "&" + i;

                for(String s : basketProducts) {
                    if(s.split("&")[0].equals(products.get(position).getId().toString())) {
                        basketProducts.remove(s);
                        break;
                    }
                }



                if(i != 0)
                    basketProducts.add(basketProduct);

                editor.putStringSet(Constants.BASKET_PRODUCTS, basketProducts);

                editor.apply();

                checkTotalPrice();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        };
    }

    private int checkBasketProducts(Product product) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        Set<String> basketProducts = preferences.getStringSet(Constants.BASKET_PRODUCTS, new TreeSet<>());

        for(String s : basketProducts) {
            String[] basketProduct = s.split("&");
            if (basketProduct[0].equals(product.getId().toString())) {
                return Integer.parseInt(basketProduct[5]);
            }
        }

        return 0;
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

        String str = totalPrice + " р";
        totalPriceView.setText(str);
    }
}
