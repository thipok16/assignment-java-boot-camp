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
        Product product1 = new Product();
        product1.setName("blueberry cheesecake");
        Product product2 = new Product();
        product2.setName("strawberry jam");
        Product product3 = new Product();
        product3.setName("english muffin");
        Product product4 = new Product();
        product4.setName("cranberry scone");
        productRepository.saveAll(
            ImmutableSet.of(product1, product2, product3, product4))
        ;

        final String keyword = "berry";
        List<Product> products = productRepository.findByNameContaining(keyword);

        Assertions.assertEquals(3, products.size());
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertTrue(products.contains(product2));
        Assertions.assertFalse(products.contains(product3));
        Assertions.assertTrue(products.contains(product4));
    }

    @Test
    void findByNameContainingNotFound() {
        Product product1 = new Product();
        product1.setName("blueberry cheesecake");
        Product product2 = new Product();
        product2.setName("strawberry jam");
        Product product3 = new Product();
        product3.setName("english muffin");
        Product product4 = new Product();
        product4.setName("cranberry scone");
        productRepository.saveAll(
            ImmutableSet.of(product1, product2, product3, product4))
        ;

        final String keyword = "washing machine";
        List<Product> products = productRepository.findByNameContaining(keyword);

        Assertions.assertTrue(products.isEmpty());
    }

}