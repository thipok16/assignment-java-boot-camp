package com.example.bakery.baskets;

import com.example.bakery.products.Product;
import com.example.bakery.products.ProductService;
import com.example.bakery.users.User;
import com.example.bakery.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public void setBasketRepository(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public List<Basket> getByUserIdAndStatus(int userId, Basket.Status status) {
        return basketRepository.findByUserIdAndStatus(userId, status);
    }

    public Basket addOrUpdateBasket(AddBasketForm addBasketForm) {
        // first, validate the userId and productId
        User user = userService.findById(addBasketForm.getUserId());
        Product product = productService.findById(addBasketForm.getProductId());

        // check that there is enough product in stock for the request
        final int amountInStock = product.getAmountInStock();
        if (amountInStock < addBasketForm.getAmount()) {
            throw new OutOfStockException(product);
        }

        List<Basket> matchedInCartBaskets = basketRepository.findByUserIdAndProductIdAndStatus(
            addBasketForm.getUserId(), addBasketForm.getProductId(), Basket.Status.INCART);

        if (matchedInCartBaskets.isEmpty()) {
            // create new basket entry
            Basket basket = new Basket();
            basket.setUser(user);
            basket.setProduct(product);
            basket.setAmount(addBasketForm.getAmount());
            basket.setStatus(Basket.Status.INCART);
            basketRepository.save(basket);
            return basket;
        } else if (matchedInCartBaskets.size() == 1) {
            // update the amount of an existing basket entry
            Basket existingBasket = matchedInCartBaskets.get(0);
            final int newAmount = addBasketForm.getAmount();
            existingBasket.setAmount(newAmount);
            basketRepository.save(existingBasket);
            return existingBasket;
        } else {
            final int numberOfEntries = matchedInCartBaskets.size();
            final String errorMessage = String.format("%s InCart baskets with user_id '%s' and product_id '%s' found",
                numberOfEntries, addBasketForm.getUserId(), addBasketForm.getProductId());
            log.error(errorMessage);
            throw new DuplicatedBasketException(errorMessage);
        }
    }
}
