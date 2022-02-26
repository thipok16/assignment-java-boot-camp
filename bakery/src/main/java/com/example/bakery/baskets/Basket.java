package com.example.bakery.baskets;

import com.example.bakery.products.Product;
import com.example.bakery.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Basket {

    public enum Status {
        INCART,
        CHECKEDOUT,
        BOUGHT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    @JsonIgnoreProperties("baskets")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @Getter
    @Setter
    @JsonIgnoreProperties("baskets")
    private Product product;

    @Getter
    @Setter
    private int amount;

    @Getter
    @Setter
    private Status status;
}
