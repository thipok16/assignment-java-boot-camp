package com.example.bakery.products;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int amountInStock;
    @Getter
    @Setter
    private String seller;

    @Getter
    @Setter
    private int priceOriginal;
    @Getter
    @Setter
    private int priceDiscount;

    @Getter
    @Setter
    private int ratingCount;
    @Getter
    @Setter
    private double ratingAverage;
}