package com.example.bakery.products;

import com.example.bakery.baskets.Basket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Product {

    @Id
    @Getter
    @Setter
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

    @OneToMany(mappedBy = "product")
    @Getter
    @Setter
    @JsonIgnore
    private List<Basket> baskets;
}