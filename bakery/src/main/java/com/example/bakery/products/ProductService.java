package com.example.bakery.products;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(int productId) {
        Optional<Product> result = productRepository.findById(productId);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ProductNotFoundException(productId);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> searchByKeyword(String keyword) {
        log.debug("keyword is {}", keyword);
        List<Product> products = productRepository.findByNameContaining(keyword);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(keyword);
        }
        return products;
    }
}
