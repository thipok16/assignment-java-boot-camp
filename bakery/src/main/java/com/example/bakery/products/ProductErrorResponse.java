package com.example.bakery.products;

import lombok.Getter;
import lombok.Setter;

public class ProductErrorResponse {

    // default constructor is required by Spring Boot
    public ProductErrorResponse() {
    }

    public ProductErrorResponse(String message) {
        this.message = message;
    }

    @Getter
    @Setter
    private String message;
}
