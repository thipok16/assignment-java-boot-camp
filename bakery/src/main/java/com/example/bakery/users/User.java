package com.example.bakery.users;

import com.example.bakery.baskets.Basket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String address;

    @OneToMany(mappedBy = "user")
    @Getter
    @Setter
    @JsonIgnore
    private List<Basket> baskets;
}
