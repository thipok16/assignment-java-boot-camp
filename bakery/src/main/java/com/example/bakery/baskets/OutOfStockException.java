package com.example.bakery.baskets;

import com.example.bakery.products.Product;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(Product product) {
        super(String.format("Product ID %s has only pieces %s left in stock.",
            product.getId(), product.getAmountInStock()));
    }
}
