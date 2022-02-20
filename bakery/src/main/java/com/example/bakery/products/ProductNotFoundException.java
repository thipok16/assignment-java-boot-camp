package com.example.bakery.products;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(int productId) {
        super(String.format("Product ID %s not found.", productId));
    }

    public ProductNotFoundException(String keyword) {
        super(String.format("No product found for keyword '%s'", keyword));
    }
}
