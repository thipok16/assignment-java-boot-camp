package com.example.bakery.baskets;

import com.example.bakery.products.Product;
import com.example.bakery.products.ProductNotFoundException;
import com.example.bakery.products.ProductService;
import com.example.bakery.users.User;
import com.example.bakery.users.UserNotFoundException;
import com.example.bakery.users.UserService;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Test
    void getByUserIdAndStatus() {
        // intentionally left blank
        // because getByUserIdAndStatus is wired directly to productRepository.getByUserIdAndStatus()
    }

    @Test
    void addBasketSuccess() {
        final int existingUserId = 7;
        User existingUser = new User();
        existingUser.setId(existingUserId);
        existingUser.setName("existing user");
        Mockito.when(userService.findById(existingUserId))
            .thenReturn(existingUser);

        final int existingProductId = 24;
        final int currentAmountInStock = 50;
        Product existingProduct = new Product();
        existingProduct.setId(existingProductId);
        existingProduct.setAmountInStock(currentAmountInStock);
        existingProduct.setName("existing product name");
        Mockito.when(productService.findById(existingProductId))
            .thenReturn(existingProduct);

        Mockito.when(basketRepository.findByUserIdAndProductIdAndStatus(existingUserId, existingProductId, Basket.Status.INCART))
            .thenReturn(ImmutableList.of());

        BasketService basketService = new BasketService();
        basketService.setBasketRepository(basketRepository);
        basketService.setUserService(userService);
        basketService.setProductService(productService);

        final int amount = 4;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, amount);
        Basket returnedBasket = basketService.addOrUpdateBasket(addBasketForm);

        Assertions.assertEquals(4, returnedBasket.getAmount());
        Assertions.assertEquals(Basket.Status.INCART, returnedBasket.getStatus());
        Assertions.assertEquals(existingUserId, returnedBasket.getUser().getId());
        Assertions.assertEquals(existingProductId, returnedBasket.getProduct().getId());
    }

    @Test
    void updateBasketSuccess() {
        final int existingUserId = 7;
        User existingUser = new User();
        existingUser.setId(existingUserId);
        existingUser.setName("existing user");
        Mockito.when(userService.findById(existingUserId))
            .thenReturn(existingUser);

        final int existingProductId = 24;
        final int currentAmountInStock = 50;
        Product existingProduct = new Product();
        existingProduct.setId(existingProductId);
        existingProduct.setAmountInStock(currentAmountInStock);
        existingProduct.setName("existing product name");
        Mockito.when(productService.findById(existingProductId))
            .thenReturn(existingProduct);

        final int currentAmountInBasket = 3;
        Basket existingBasket = new Basket();
        existingBasket.setUser(existingUser);
        existingBasket.setProduct(existingProduct);
        existingBasket.setAmount(currentAmountInBasket);
        existingBasket.setStatus(Basket.Status.INCART);
        Mockito.when(basketRepository.findByUserIdAndProductIdAndStatus(existingUserId, existingProductId, Basket.Status.INCART))
            .thenReturn(ImmutableList.of(existingBasket));

        BasketService basketService = new BasketService();
        basketService.setBasketRepository(basketRepository);
        basketService.setUserService(userService);
        basketService.setProductService(productService);

        final int newAmount = 5;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, newAmount);
        Basket returnedBasket = basketService.addOrUpdateBasket(addBasketForm);

        Assertions.assertEquals(newAmount, returnedBasket.getAmount());
        Assertions.assertEquals(Basket.Status.INCART, returnedBasket.getStatus());
    }

    @Test
    void addOrUpdateBasketUserNotFound() {
        final int notExistingUserId = 9;
        Mockito.when(userService.findById(notExistingUserId))
            .thenThrow(new UserNotFoundException(notExistingUserId));

        BasketService basketService = new BasketService();
        basketService.setBasketRepository(basketRepository);
        basketService.setUserService(userService);
        basketService.setProductService(productService);

        final int existingProductId = 24;
        final int amount = 4;
        AddBasketForm addBasketForm = new AddBasketForm(notExistingUserId, existingProductId, amount);
        UserNotFoundException exception = Assertions.assertThrows(
            UserNotFoundException.class,
            () -> basketService.addOrUpdateBasket(addBasketForm));
        Assertions.assertEquals(
            String.format("User ID %s not found.", notExistingUserId),
            exception.getMessage());
    }

    @Test
    void addOrUpdateBasketProductNotFound() {
        final int existingUserId = 7;
        User existingUser = new User();
        existingUser.setId(existingUserId);
        existingUser.setName("existing user");
        Mockito.when(userService.findById(existingUserId))
            .thenReturn(existingUser);

        final int notExistingProductId = 5;
        Mockito.when(productService.findById(notExistingProductId))
            .thenThrow(new ProductNotFoundException(notExistingProductId));

        BasketService basketService = new BasketService();
        basketService.setBasketRepository(basketRepository);
        basketService.setUserService(userService);
        basketService.setProductService(productService);

        final int amount = 4;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, notExistingProductId, amount);
        ProductNotFoundException exception = Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> basketService.addOrUpdateBasket(addBasketForm));
        Assertions.assertEquals(
            String.format("Product ID %s not found.", notExistingProductId),
            exception.getMessage());
    }

    @Test
    void addOrUpdateBasketOutOfStock() {
        final int existingUserId = 7;
        User existingUser = new User();
        existingUser.setId(existingUserId);
        existingUser.setName("existing user");
        Mockito.when(userService.findById(existingUserId))
            .thenReturn(existingUser);

        final int existingProductId = 24;
        final int currentAmountInStock = 2;
        Product existingProduct = new Product();
        existingProduct.setId(existingProductId);
        existingProduct.setAmountInStock(currentAmountInStock);
        existingProduct.setName("existing product name");
        Mockito.when(productService.findById(existingProductId))
            .thenReturn(existingProduct);

        BasketService basketService = new BasketService();
        basketService.setBasketRepository(basketRepository);
        basketService.setUserService(userService);
        basketService.setProductService(productService);

        final int amount = 9;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, amount);
        OutOfStockException exception = Assertions.assertThrows(
            OutOfStockException.class,
            () -> basketService.addOrUpdateBasket(addBasketForm));
        Assertions.assertEquals(
            String.format("Product ID %s has only %s piece(s) left in stock.", existingProductId, currentAmountInStock),
            exception.getMessage());
    }

    @Test
    void addOrUpdateBasketDuplicatedBasketFailure() {
        final int existingUserId = 7;
        User existingUser = new User();
        existingUser.setId(existingUserId);
        existingUser.setName("existing user");
        Mockito.when(userService.findById(existingUserId))
            .thenReturn(existingUser);

        final int existingProductId = 24;
        final int currentAmountInStock = 50;
        Product existingProduct = new Product();
        existingProduct.setId(existingProductId);
        existingProduct.setAmountInStock(currentAmountInStock);
        existingProduct.setName("existing product name");
        Mockito.when(productService.findById(existingProductId))
            .thenReturn(existingProduct);

        final int numberOfEntries = 2;
        final int currentAmountInBasket1 = 3;
        Basket existingBasket1 = new Basket();
        existingBasket1.setUser(existingUser);
        existingBasket1.setProduct(existingProduct);
        existingBasket1.setAmount(currentAmountInBasket1);
        existingBasket1.setStatus(Basket.Status.INCART);
        final int currentAmountInBasket2 = 4;
        Basket existingBasket2 = new Basket();
        existingBasket2.setUser(existingUser);
        existingBasket2.setProduct(existingProduct);
        existingBasket2.setAmount(currentAmountInBasket2);
        existingBasket2.setStatus(Basket.Status.INCART);
        Mockito.when(basketRepository.findByUserIdAndProductIdAndStatus(existingUserId, existingProductId, Basket.Status.INCART))
            .thenReturn(ImmutableList.of(existingBasket1, existingBasket2));

        BasketService basketService = new BasketService();
        basketService.setBasketRepository(basketRepository);
        basketService.setUserService(userService);
        basketService.setProductService(productService);

        final int amount = 9;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, amount);
        DuplicatedBasketException exception = Assertions.assertThrows(
            DuplicatedBasketException.class,
            () -> basketService.addOrUpdateBasket(addBasketForm));
        Assertions.assertEquals(
            String.format("%s InCart baskets with user_id '%s' and product_id '%s' found",
                numberOfEntries, addBasketForm.getUserId(), addBasketForm.getProductId()),
            exception.getMessage());
    }
}