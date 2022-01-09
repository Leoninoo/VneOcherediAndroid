package com.leonino.vneocherediandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.leonino.vneocherediandroid.R;
import com.leonino.vneocherediandroid.adapters.CategoryAdapter;
import com.leonino.vneocherediandroid.fragments.HeaderFragment;
import com.leonino.vneocherediandroid.models.CategoryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Создаем 2 recycler view для категорий и заполняем их
        List<CategoryItem> firstCategoryItems = getFirstCategoryItems();
        List<CategoryItem> secondCategoryItems = getSecondCategoryItems();

        RecyclerView firstLineCategories = findViewById(R.id.first_line_categories);
        RecyclerView secondLineCategories = findViewById(R.id.second_line_categories);

        View headerView = findViewById(R.id.top_login_box);

        CategoryAdapter firstAdapter = new CategoryAdapter(this, firstCategoryItems, false, headerView);
        CategoryAdapter secondAdapter = new CategoryAdapter(this, secondCategoryItems, false, headerView);

        RecyclerView.LayoutManager firstLayoutManager
                = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager secondLayoutManager
                = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        firstLineCategories.setLayoutManager(firstLayoutManager);
        secondLineCategories.setLayoutManager(secondLayoutManager);

        firstLineCategories.setAdapter(firstAdapter);
        secondLineCategories.setAdapter(secondAdapter);

        firstLineCategories.scrollToPosition(Short.MAX_VALUE / 2);
        secondLineCategories.scrollToPosition(Short.MAX_VALUE / 2);

        //Задаем анимацию движения recycler view (костыль)
        Animation animationFirstCategories =
                AnimationUtils.loadAnimation(this, R.anim.motion_categories1);
        Animation animationSecondCategories =
                AnimationUtils.loadAnimation(this, R.anim.motion_categories2);
        animationFirstCategories.setRepeatCount(5);
        animationSecondCategories.setRepeatCount(5);

        firstLineCategories.setAnimation(animationFirstCategories);
        secondLineCategories.setAnimation(animationSecondCategories);

        //Нормальный метод авто прокрутки recycler view. По этом варианте не работают лисенеры элементов
//        final int scrollSpeed = 2000;
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                firstLineCategories.smoothScrollBy(100, 0, new LinearInterpolator(), 10000);
//                secondLineCategories.smoothScrollBy(-100,0, new LinearInterpolator(), 10000);
//
//                handler.postDelayed(this, scrollSpeed);
//            }
//        };
//        handler.postDelayed(runnable, scrollSpeed);


        //Создаем header и передаем туда флаг, если мы только что залогинились
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HeaderFragment headerFragment = new HeaderFragment(this);
        Bundle args = new Bundle();
        args.putBoolean("flagLogin", getIntent().getBooleanExtra("flagLogin", true));
        headerFragment.setArguments(args);
        transaction.add(R.id.top_login_box, headerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private List<CategoryItem> getFirstCategoryItems() {
        List<CategoryItem> items = new ArrayList<>();

        items.add(new CategoryItem(R.drawable.ic_vegetables, "Овощи"));
        items.add(new CategoryItem(R.drawable.ic_milk, "Молоко"));
        items.add(new CategoryItem(R.drawable.ic_meat, "Мясо"));
        items.add(new CategoryItem(R.drawable.ic_fish, "Рыба"));

        return items;
    }

    private List<CategoryItem> getSecondCategoryItems() {
        List<CategoryItem> items = new ArrayList<>();

        items.add(new CategoryItem(R.drawable.ic_vegetables, "Чай"));
        items.add(new CategoryItem(R.drawable.ic_milk, "Сладости"));
        items.add(new CategoryItem(R.drawable.ic_meat, "Пиво"));
        items.add(new CategoryItem(R.drawable.ic_fish, "Наркотики"));

        return items;
    }
}