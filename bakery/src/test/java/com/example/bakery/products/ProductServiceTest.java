package com.example.bakery.products;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void findByIdSuccess() {
        final int existedProductId = 1;
        Product mockProduct = new Product();
        mockProduct.setName("existing product name");
        Mockito.when(productRepository.findById(existedProductId))
            .thenReturn(Optional.of(mockProduct));

        ProductService productService = new ProductService();
        productService.setProductRepository(productRepository);
        Product product = productService.findById(existedProductId);

        Assertions.assertEquals("existing product name", product.getName());
    }

    @Test
    void findByIdNotFound() {
        final int notExistedProductId = 999;
        Mockito.when(productRepository.findById(notExistedProductId))
            .thenThrow(new ProductNotFoundException(notExistedProductId));

        ProductService productService = new ProductService();
        productService.setProductRepository(productRepository);

        ProductNotFoundException exception = Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> productService.findById(notExistedProductId));
        Assertions.assertEquals(
            String.format("Product ID %s not found.", notExistedProductId),
            exception.getMessage());
    }

    @Test
    void findAll() {
        // intentionally left blank
        // because findAll is wired directly to productRepository.findAll()
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

        ProductService productService = new ProductService();
        productService.setProductRepository(productRepository);
        List<Product> products = productService.searchByKeyword(existedKeyword);

        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.contains(mockProduct1));
        Assertions.assertTrue(products.contains(mockProduct2));
    }

    @Test
    void searchByKeywordNotFound() {
        final String notExistedKeyword = "something";
        Mockito.when(productRepository.findByNameContaining(notExistedKeyword))
            .thenThrow(new ProductNotFoundException(notExistedKeyword));

        ProductService productService = new ProductService();
        productService.setProductRepository(productRepository);

        ProductNotFoundException exception = Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> productService.searchByKeyword(notExistedKeyword));
        Assertions.assertEquals(
            String.format("No product found for keyword '%s'", notExistedKeyword),
            exception.getMessage());
    }
}