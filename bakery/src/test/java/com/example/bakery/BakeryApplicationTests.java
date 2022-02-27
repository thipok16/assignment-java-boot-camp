package com.example.bakery;

import com.example.bakery.baskets.AddBasketForm;
import com.example.bakery.baskets.Basket;
import com.example.bakery.baskets.BasketErrorResponse;
import com.example.bakery.products.Product;
import com.example.bakery.products.ProductErrorResponse;
import com.example.bakery.users.UserErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BakeryApplicationTests {

    // all tests in this class depend on the data from loadInitProducts and loadInitUsers

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

        Assertions.assertEquals(7, products.size());
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

    @Test
    void addThenUpdateBasketSuccess() {
        final int existingUserId = 3;
        final int existingProductId = 5;
        final int amount = 1;
        // a user add something to the basket
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<Basket> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            Basket.class);

        Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        final Basket basket = putResponse.getBody();
        Assertions.assertEquals(amount, basket.getAmount());
        Assertions.assertEquals(existingUserId, basket.getUser().getId());
        Assertions.assertEquals(existingProductId, basket.getProduct().getId());

        // the user changes her mind and update the basket amount to 4
        final int newAmount = 4;
        AddBasketForm newAddBasketForm = new AddBasketForm(existingUserId, existingProductId, newAmount);
        HttpEntity<AddBasketForm> newPutRequest = new HttpEntity<>(newAddBasketForm);
        ResponseEntity<Basket> newPutResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            newPutRequest,
            Basket.class);

        Assertions.assertEquals(HttpStatus.OK, newPutResponse.getStatusCode());
        final Basket updatedBasket = newPutResponse.getBody();
        Assertions.assertEquals(newAmount, updatedBasket.getAmount());
        Assertions.assertEquals(existingUserId, updatedBasket.getUser().getId());
        Assertions.assertEquals(existingProductId, updatedBasket.getProduct().getId());
    }

    @Test
    void addOrUpdateBasketUserNotFound() {
        final int notExistingUserId = 999;
        final int existingProductId = 4;
        final int amount = 1;
        AddBasketForm addBasketForm = new AddBasketForm(notExistingUserId, existingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<UserErrorResponse> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            UserErrorResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, putResponse.getStatusCode());
        UserErrorResponse userErrorResponse = putResponse.getBody();
        Assertions.assertEquals(
            String.format("User ID %s not found.", notExistingUserId),
            userErrorResponse.getMessage());
    }

    @Test
    void addOrUpdateBasketProductNotFound() {
        final int existingUserId = 2;
        final int notExistingProductId = 998;
        final int amount = 4;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, notExistingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<ProductErrorResponse> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            ProductErrorResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, putResponse.getStatusCode());
        ProductErrorResponse productErrorResponse = putResponse.getBody();
        Assertions.assertEquals(
            String.format("Product ID %s not found.", notExistingProductId),
            productErrorResponse.getMessage());
    }

    @Test
    void addOrUpdateBasketOutOfStock() {
        final int existingUserId = 2;
        final int existingProductId = 4;

        Product product = testRestTemplate.getForObject(
            "/products/" + existingProductId,
            Product.class);
        final int currentAmountInStock = product.getAmountInStock();
        final int veryLargeAmount = currentAmountInStock + 9999;

        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, veryLargeAmount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<BasketErrorResponse> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            BasketErrorResponse.class);
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, putResponse.getStatusCode());
        BasketErrorResponse basketErrorResponse = putResponse.getBody();
        Assertions.assertEquals(
            String.format("Product ID %s has only %s piece(s) left in stock.",
                existingProductId, currentAmountInStock),
            basketErrorResponse.getMessage());
    }
}
