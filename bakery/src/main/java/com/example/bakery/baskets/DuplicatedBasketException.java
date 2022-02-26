package com.example.bakery.baskets;

public class DuplicatedBasketException extends RuntimeException {

    public DuplicatedBasketException(String message) {
        super(message);
    }
}
