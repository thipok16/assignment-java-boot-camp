package com.example.bakery.baskets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping("/baskets/inCart")
    public List<Basket> getBasketsInCartForUser(@RequestParam("userId") int userId) {
        return basketService.getByUserIdAndStatus(userId, Basket.Status.INCART);
    }

    @GetMapping("/baskets/checkedOut")
    public List<Basket> getBasketsCheckedOutForUser(@RequestParam("userId") int userId) {
        return basketService.getByUserIdAndStatus(userId, Basket.Status.CHECKEDOUT);
    }

    @GetMapping("/baskets/bought")
    public List<Basket> getBasketsBoughtForUser(@RequestParam("userId") int userId) {
        return basketService.getByUserIdAndStatus(userId, Basket.Status.BOUGHT);
    }

    @PutMapping("/baskets")
    public Basket updateBasket(@RequestBody AddBasketForm addBasketForm) {
        return basketService.addOrUpdateBasket(addBasketForm);
    }
}
