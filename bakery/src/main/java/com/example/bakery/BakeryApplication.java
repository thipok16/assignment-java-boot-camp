package com.example.bakery;

import com.example.bakery.products.Product;
import com.example.bakery.products.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
public class BakeryApplication {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void loadInitData() {
        Product mushroomPuff = new Product();
        mushroomPuff.setName("mushroom puff");
        mushroomPuff.setSeller("thai airways");
        mushroomPuff.setAmountInStock(30);
        productRepository.save(mushroomPuff);

        Product chickenPanini = new Product();
        chickenPanini.setName("chicken panini");
        chickenPanini.setSeller("holey");
        chickenPanini.setAmountInStock(5);
        productRepository.save(chickenPanini);

        Product beefPanini = new Product();
        beefPanini.setName("fattie pattie panini");
        beefPanini.setSeller("holey");
        beefPanini.setAmountInStock(7);
        productRepository.save(beefPanini);
    }

    public static void main(String[] args) {
        SpringApplication.run(BakeryApplication.class, args);
    }

}
