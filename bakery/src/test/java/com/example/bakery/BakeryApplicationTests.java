package com.example.bakery;

import com.example.bakery.products.Product;
import com.example.bakery.products.ProductErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BakeryApplicationTests {

    // all tests in this class depend on the data from loadInitData

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void getProductByIdSuccess() {
        final int productId = 1;
        ResponseEntity<Product> response = testRestTemplate.getForEntity(
            "/products/" + productId,
            Product.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Product product = response.getBody();
        Assertions.assertEquals("mushroom puff", product.getName());
    }

    @Test
    void getProductByIdNotFound() {
        final int productId = 999;
        ResponseEntity<ProductErrorResponse> response = testRestTemplate.getForEntity(
            "/products/" + productId,
            ProductErrorResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ProductErrorResponse productErrorResponse = response.getBody();
        Assertions.assertEquals(
            String.format("Product ID %s not found.", productId),
            productErrorResponse.getMessage());
    }

    @Test
    void getAllProductsSuccess() {
        ResponseEntity<Product[]> response = testRestTemplate.getForEntity(
            "/products",
            Product[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Product> products = Arrays.asList(response.getBody());

        Assertions.assertEquals(3, products.size());
    }

    @Test
    void searchByKeywordFound() {
        final String keyword = "panini";
        ResponseEntity<Product[]> response = testRestTemplate.getForEntity(
            "/products/search?keyword=" + keyword,
            Product[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Product> products = Arrays.asList(response.getBody());
        Assertions.assertEquals(2, products.size());
        for (Product product : products) {
            final String productName = product.getName();
            Assertions.assertTrue(productName.contains(keyword));
        }
    }

    @Test
    void searchByKeywordNotFound() {
        final String keyword = "tuvwxyz";
        ResponseEntity<ProductErrorResponse> response = testRestTemplate.getForEntity(
            "/products/search?keyword=" + keyword,
            ProductErrorResponse.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ProductErrorResponse productErrorResponse = response.getBody();
        Assertions.assertEquals(
            String.format("No product found for keyword '%s'", keyword),
            productErrorResponse.getMessage());
    }
}
