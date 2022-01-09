package com.leonino.vneocherediandroid.models;

import android.media.Image;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryItem {
    private int image;
    private String name;
    private boolean isChosen;

    public CategoryItem(int image, String name) {
        this.image = image;
        this.name = name;
    }
}
