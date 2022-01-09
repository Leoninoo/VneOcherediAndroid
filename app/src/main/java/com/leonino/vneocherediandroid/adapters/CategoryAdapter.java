package com.leonino.vneocherediandroid.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.activities.CatalogActivity;
import com.leonino.vneocherediandroid.models.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private final List<CategoryItem> categoryItems;
    private final Activity context;
    private final boolean isCatalog;

    private View headerView;

    public CategoryAdapter(Activity context, List<CategoryItem> categoryItems, boolean catalog, View view) {
        this.categoryItems = categoryItems;
        this.context = context;
        this.isCatalog = catalog;
        this.headerView = view;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;

        if(isCatalog)
             view = LayoutInflater.from(context).inflate(R.layout.item_catalog_category, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_main_category, parent, false);

        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        CategoryItem item = categoryItems.get(position % categoryItems.size());

        holder.name.setText(item.getName());
        holder.image.setImageResource(item.getImage());
        holder.image.setTransitionName(item.getName());

        holder.itemView.setOnClickListener(view -> {
            view.setTransitionName(translate(item.getName()));

            Intent intent = new Intent(context, CatalogActivity.class);

            intent.putExtra("category", item.getName());

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    context, new Pair<>(headerView, "tansN"));

            context.startActivity(intent, options.toBundle());

            if(isCatalog)
                context.finish();
        });

        if(item.isChosen()) {
            holder.layout.setBackgroundColor(Color.parseColor("#532A9F"));
            holder.name.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    private String translate(String category) {
        switch (category) {
            case "Овощи":
                return "veg";

            case "Молоко":
                return "milk";

            case "Мясо":
                return "meat";

            case "Рыба":
                return "fish";

            default: return "any";
        }
    }

    @Override
    public int getItemCount() {
        if(isCatalog)
            return categoryItems.size();

        return Short.MAX_VALUE;
    }


    protected static final class CategoryHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView name;
        private final View itemView;
        private final LinearLayout layout;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            image = itemView.findViewById(R.id.img_category_main);
            name = itemView.findViewById(R.id.name_category_main);
            layout = itemView.findViewById(R.id.item_layout);
        }
    }
}
