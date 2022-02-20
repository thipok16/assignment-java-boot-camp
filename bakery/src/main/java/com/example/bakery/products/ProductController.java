package com.example.bakery.products;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getAll() {
        return productService.findAll();
    }

    @GetMapping("/products/{productId}")
    public Product getOne(@PathVariable int productId) {
        return productService.findById(productId);
    }

    @GetMapping("/products/search")
    public List<Product> search(@RequestParam("keyword") String keyword) {
        log.debug("keyword is {}", keyword);
        return productService.searchByKeyword(keyword);
    }
}
