package com.example.bakery.products;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void getProductByIdSuccess() {
        final int existedProductId = 1;
        Product mockProduct = new Product();
        mockProduct.setName("existing product name");
        Mockito.when(productRepository.findById(existedProductId))
            .thenReturn(Optional.of(mockProduct));

        ResponseEntity<Product> response = testRestTemplate.getForEntity(
            "/products/" + existedProductId,
            Product.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Product product = response.getBody();
        Assertions.assertEquals("existing product name", product.getName());
    }

    @Test
    void getProductByIdNotFound() {
        final int notExistedProductId = 999;
        Mockito.when(productRepository.findById(notExistedProductId))
            .thenThrow(new ProductNotFoundException(notExistedProductId));

        ResponseEntity<ProductErrorResponse> response = testRestTemplate.getForEntity(
            "/products/" + notExistedProductId,
            ProductErrorResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ProductErrorResponse productErrorResponse = response.getBody();
        Assertions.assertEquals(
            String.format("Product ID %s not found.", notExistedProductId),
            productErrorResponse.getMessage());
    }

    @Test
    void getAllProductsSuccess() {
        Product mockProduct1 = new Product();
        mockProduct1.setName("existing product name 1");
        Product mockProduct2 = new Product();
        mockProduct2.setName("existing product name 2");
        Product mockProduct3 = new Product();
        mockProduct3.setName("existing product name 3");
        List<Product> mockProducts = ImmutableList.of(mockProduct1, mockProduct2, mockProduct3);
        Mockito.when(productRepository.findAll())
            .thenReturn(mockProducts);

        ResponseEntity<Product[]> response = testRestTemplate.getForEntity(
            "/products",
            Product[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Product> products = Arrays.asList(response.getBody());
        Assertions.assertEquals(3, products.size());
    }

    @Test
    void searchByKeywordFound() {
        final String existedKeyword = "something";
        Product mockProduct1 = new Product();
        mockProduct1.setName("something sandwich");
        Product mockProduct2 = new Product();
        mockProduct2.setName("crepe with something");
        List<Product> mockProducts = ImmutableList.of(mockProduct1, mockProduct2);
        Mockito.when(productRepository.findByNameContaining(existedKeyword))
            .thenReturn(mockProducts);

        ResponseEntity<Product[]> response = testRestTemplate.getForEntity(
            "/products/search?keyword=" + existedKeyword,
            Product[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Product> products = Arrays.asList(response.getBody());
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals(mockProduct1.getName(), products.get(0).getName());
        Assertions.assertEquals(mockProduct2.getName(), products.get(1).getName());
    }

    @Test
    void searchByKeywordNotFound() {
        final String notExistedKeyword = "something";
        Mockito.when(productRepository.findByNameContaining(notExistedKeyword))
            .thenThrow(new ProductNotFoundException(notExistedKeyword));

        ResponseEntity<ProductErrorResponse> response = testRestTemplate.getForEntity(
            "/products/search?keyword=" + notExistedKeyword,
            ProductErrorResponse.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ProductErrorResponse productErrorResponse = response.getBody();
        Assertions.assertEquals(
            String.format("No product found for keyword '%s'", notExistedKeyword),
            productErrorResponse.getMessage());
    }
}