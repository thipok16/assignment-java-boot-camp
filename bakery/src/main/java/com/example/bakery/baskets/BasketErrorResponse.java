package com.example.bakery.baskets;

import lombok.Getter;
import lombok.Setter;

public class BasketErrorResponse {

    // default constructor is required by Spring Boot
    public BasketErrorResponse() {
    }

    public BasketErrorResponse(String message) {
        this.message = message;
    }

    @Getter
    @Setter
    private String message;
}
