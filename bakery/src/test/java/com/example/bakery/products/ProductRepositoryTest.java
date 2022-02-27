package com.example.bakery.products;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByNameContaining() {
        // use loaded product data in loadInitProducts
        final String keyword = "berry";
        List<Product> products = productRepository.findByNameContaining(keyword);

        Assertions.assertEquals(3, products.size());
        for (Product product : products) {
            Assertions.assertTrue(product.getName().contains(keyword));
        }
    }

    @Test
    void findByNameContainingNotFound() {
        // use loaded product data in loadInitProducts
        final String keyword = "washing machine";
        List<Product> products = productRepository.findByNameContaining(keyword);

        Assertions.assertTrue(products.isEmpty());
    }

}