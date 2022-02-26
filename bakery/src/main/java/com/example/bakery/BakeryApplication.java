package com.example.bakery;

import com.example.bakery.products.Product;
import com.example.bakery.products.ProductRepository;
import com.example.bakery.users.User;
import com.example.bakery.users.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void loadInitProducts() {
        Product mushroomPuff = new Product();
        mushroomPuff.setId(1);
        mushroomPuff.setName("mushroom puff");
        mushroomPuff.setSeller("thai airways");
        mushroomPuff.setAmountInStock(30);
        productRepository.save(mushroomPuff);

        Product chickenPanini = new Product();
        chickenPanini.setId(2);
        chickenPanini.setName("chicken panini");
        chickenPanini.setSeller("holey");
        chickenPanini.setAmountInStock(5);
        productRepository.save(chickenPanini);

        Product beefPanini = new Product();
        beefPanini.setId(3);
        beefPanini.setName("fattie pattie panini");
        beefPanini.setSeller("holey");
        beefPanini.setAmountInStock(7);
        productRepository.save(beefPanini);
    }

    @PostConstruct
    public void loadInitUsers() {
        User harry = new User();
        harry.setId(1);
        harry.setUsername("user1");
        harry.setPassword("password1");
        harry.setName("Harry Potter");
        harry.setAddress("4 Privet Drive");
        userRepository.save(harry);

        User ron = new User();
        ron.setId(2);
        ron.setUsername("user2");
        ron.setPassword("password2");
        ron.setName("Ron Weasley");
        ron.setAddress("The Burrow");
        userRepository.save(ron);

        User hermione = new User();
        hermione.setId(3);
        hermione.setUsername("user3");
        hermione.setPassword("password3");
        hermione.setName("Hermione Granger");
        hermione.setAddress("A dentist clinic");
        userRepository.save(hermione);
    }

    public static void main(String[] args) {
        SpringApplication.run(BakeryApplication.class, args);
    }

}
