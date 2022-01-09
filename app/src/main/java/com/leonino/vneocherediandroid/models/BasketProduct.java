package com.leonino.vneocherediandroid.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasketProduct {
    private Long id;
    private String image;
    private String name;
    private String price;
    private int counter;
}
