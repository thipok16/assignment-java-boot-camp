package com.example.bakery.baskets;

import com.example.bakery.products.Product;
import com.example.bakery.products.ProductRepository;
import com.example.bakery.users.User;
import com.example.bakery.users.UserRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class BasketRepositoryTest {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByUserIdAndStatus() {
        // retrieve loaded user data
        User user1 = userRepository.getById(1);
        User user2 = userRepository.getById(2);
        User user3 = userRepository.getById(3);

        // retrieve loaded product data
        Product product1 = productRepository.getById(1);
        Product product2 = productRepository.getById(2);
        Product product3 = productRepository.getById(3);
        Product product4 = productRepository.getById(4);

        Basket basket1 = new Basket();
        basket1.setUser(user1);
        basket1.setProduct(product1);
        basket1.setAmount(1);
        basket1.setStatus(Basket.Status.INCART);
        Basket basket2 = new Basket();
        basket2.setUser(user1);
        basket2.setProduct(product2);
        basket2.setAmount(2);
        basket2.setStatus(Basket.Status.CHECKEDOUT);
        Basket basket3 = new Basket();
        basket3.setUser(user1);
        basket3.setProduct(product3);
        basket3.setAmount(3);
        basket3.setStatus(Basket.Status.BOUGHT);
        Basket basket4 = new Basket();
        basket4.setUser(user2);
        basket4.setProduct(product3);
        basket4.setAmount(4);
        basket4.setStatus(Basket.Status.INCART);
        Basket basket5 = new Basket();
        basket5.setUser(user2);
        basket5.setProduct(product4);
        basket5.setAmount(5);
        basket5.setStatus(Basket.Status.INCART);
        basketRepository.saveAll(
            ImmutableSet.of(basket1, basket2, basket3, basket4, basket5));

        final int user1Id = user1.getId();
        List<Basket> user1InCartBaskets = basketRepository.findByUserIdAndStatus(user1Id, Basket.Status.INCART);
        List<Basket> user1CheckedOutBaskets = basketRepository.findByUserIdAndStatus(user1Id, Basket.Status.CHECKEDOUT);
        List<Basket> user1BoughtBaskets = basketRepository.findByUserIdAndStatus(user1Id, Basket.Status.BOUGHT);

        Assertions.assertEquals(1, user1InCartBaskets.size());
        Basket user1InCartBasket = user1InCartBaskets.get(0);
        Assertions.assertEquals(user1.getName(), user1InCartBasket.getUser().getName());
        Assertions.assertEquals(product1.getName(), user1InCartBasket.getProduct().getName());
        Assertions.assertEquals(1, user1InCartBasket.getAmount());

        Assertions.assertEquals(1, user1CheckedOutBaskets.size());
        Basket user1CheckedOutBasket = user1CheckedOutBaskets.get(0);
        Assertions.assertEquals(user1.getName(), user1CheckedOutBasket.getUser().getName());
        Assertions.assertEquals(product2.getName(), user1CheckedOutBasket.getProduct().getName());
        Assertions.assertEquals(2, user1CheckedOutBasket.getAmount());

        Assertions.assertEquals(1, user1BoughtBaskets.size());
        Basket user1BoughtBasket = user1BoughtBaskets.get(0);
        Assertions.assertEquals(user1.getName(), user1BoughtBasket.getUser().getName());
        Assertions.assertEquals(product3.getName(), user1BoughtBasket.getProduct().getName());
        Assertions.assertEquals(3, user1BoughtBasket.getAmount());

        final int user2Id = user2.getId();
        List<Basket> user2InCartBaskets = basketRepository.findByUserIdAndStatus(user2Id, Basket.Status.INCART);
        Assertions.assertEquals(2, user2InCartBaskets.size());

        final int user3Id = user3.getId();
        List<Basket> user3InCartBaskets = basketRepository.findByUserIdAndStatus(user3Id, Basket.Status.INCART);
        Assertions.assertTrue(user3InCartBaskets.isEmpty());
    }

    @Test
    void findByUserIdAndProductIdAndStatus() {
        // retrieve loaded user data
        User user1 = userRepository.getById(1);
        User user2 = userRepository.getById(2);
        User user3 = userRepository.getById(3);

        // retrieve loaded product data
        Product product1 = productRepository.getById(1);
        Product product2 = productRepository.getById(2);

        Basket basket1 = new Basket();
        basket1.setUser(user1);
        basket1.setProduct(product1);
        basket1.setAmount(1);
        basket1.setStatus(Basket.Status.INCART);
        Basket basket2 = new Basket();
        basket2.setUser(user1);
        basket2.setProduct(product1);
        basket2.setAmount(2);
        basket2.setStatus(Basket.Status.CHECKEDOUT);
        Basket basket3 = new Basket();
        basket3.setUser(user2);
        basket3.setProduct(product2);
        basket3.setAmount(3);
        basket3.setStatus(Basket.Status.BOUGHT);
        Basket basket4 = new Basket();
        basket4.setUser(user2);
        basket4.setProduct(product2);
        basket4.setAmount(4);
        basket4.setStatus(Basket.Status.BOUGHT);
        basketRepository.saveAll(
            ImmutableSet.of(basket1, basket2, basket3, basket4));

        List<Basket> user1Product1InCartBaskets = basketRepository.findByUserIdAndProductIdAndStatus(
            user1.getId(), product1.getId(), Basket.Status.INCART);
        Assertions.assertEquals(1, user1Product1InCartBaskets.size());

        List<Basket> user1Product1CheckedOutBaskets = basketRepository.findByUserIdAndProductIdAndStatus(
            user1.getId(), product1.getId(), Basket.Status.CHECKEDOUT);
        Assertions.assertEquals(1, user1Product1CheckedOutBaskets.size());

        List<Basket> user1Product2CheckedOutBaskets = basketRepository.findByUserIdAndProductIdAndStatus(
            user1.getId(), product2.getId(), Basket.Status.CHECKEDOUT);
        Assertions.assertEquals(0, user1Product2CheckedOutBaskets.size());

        List<Basket> user2Product2BoughtBaskets = basketRepository.findByUserIdAndProductIdAndStatus(
            user2.getId(), product2.getId(), Basket.Status.BOUGHT);
        Assertions.assertEquals(2, user2Product2BoughtBaskets.size());

        List<Basket> user3Product1InCartBaskets = basketRepository.findByUserIdAndProductIdAndStatus(
            user3.getId(), product1.getId(), Basket.Status.INCART);
        Assertions.assertEquals(0, user3Product1InCartBaskets.size());
    }
}