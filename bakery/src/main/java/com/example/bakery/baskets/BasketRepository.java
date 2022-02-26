package com.example.bakery.baskets;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Integer> {

    List<Basket> findByUserIdAndStatus(int userId, Basket.Status status);

    List<Basket> findByUserIdAndProductIdAndStatus(int userId, int productId, Basket.Status status);
}
