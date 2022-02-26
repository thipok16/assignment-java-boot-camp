package com.example.bakery.baskets;

import com.example.bakery.products.Product;
import com.example.bakery.products.ProductErrorResponse;
import com.example.bakery.products.ProductNotFoundException;
import com.example.bakery.products.ProductService;
import com.example.bakery.users.User;
import com.example.bakery.users.UserErrorResponse;
import com.example.bakery.users.UserNotFoundException;
import com.example.bakery.users.UserService;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasketControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @Test
    void getBasketsForUser() {
        User user1 = new User();
        user1.setName("user1 name");
        User user2 = new User();
        user2.setName("user2 name");
        User user3 = new User();
        user3.setName("user3 name");

        Product product1 = new Product();
        product1.setName("blueberry cheesecake");
        Product product2 = new Product();
        product2.setName("strawberry jam");
        Product product3 = new Product();
        product3.setName("english muffin");
        Product product4 = new Product();
        product4.setName("cranberry scone");

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

        final int user1Id = user1.getId();
        // getBasketsInCartForUser
        Mockito.when(basketRepository.findByUserIdAndStatus(user1Id, Basket.Status.INCART))
            .thenReturn(ImmutableList.of(basket1));

        ResponseEntity<Basket[]> inCartResponse = testRestTemplate.getForEntity(
            "/baskets/inCart?userId=" + user1Id,
            Basket[].class);

        Assertions.assertEquals(HttpStatus.OK, inCartResponse.getStatusCode());
        List<Basket> inCartBaskets = Arrays.asList(inCartResponse.getBody());
        Assertions.assertEquals(1, inCartBaskets.size());
        Assertions.assertEquals(basket1.getAmount(), inCartBaskets.get(0).getAmount());

        // getBasketsCheckedOutForUser
        Mockito.when(basketRepository.findByUserIdAndStatus(user1Id, Basket.Status.CHECKEDOUT))
            .thenReturn(ImmutableList.of(basket2));

        ResponseEntity<Basket[]> checkedOutResponse = testRestTemplate.getForEntity(
            "/baskets/checkedOut?userId=" + user1Id,
            Basket[].class);

        Assertions.assertEquals(HttpStatus.OK, checkedOutResponse.getStatusCode());
        List<Basket> checkedOutBaskets = Arrays.asList(checkedOutResponse.getBody());
        Assertions.assertEquals(1, checkedOutBaskets.size());
        Assertions.assertEquals(basket2.getAmount(), checkedOutBaskets.get(0).getAmount());

        // getBasketsBoughtForUser
        Mockito.when(basketRepository.findByUserIdAndStatus(user1Id, Basket.Status.BOUGHT))
            .thenReturn(ImmutableList.of(basket3));

        ResponseEntity<Basket[]> boughtResponse = testRestTemplate.getForEntity(
            "/baskets/bought?userId=" + user1Id,
            Basket[].class);

        Assertions.assertEquals(HttpStatus.OK, boughtResponse.getStatusCode());
        List<Basket> boughtBaskets = Arrays.asList(boughtResponse.getBody());
        Assertions.assertEquals(1, boughtBaskets.size());
        Assertions.assertEquals(basket3.getAmount(), boughtBaskets.get(0).getAmount());
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

        final int amount = 2;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<Basket> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            Basket.class);

        Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        final Basket basket = putResponse.getBody();
        Assertions.assertEquals(amount, basket.getAmount());
        Assertions.assertEquals(existingUserId, basket.getUser().getId());
        Assertions.assertEquals(existingProductId, basket.getProduct().getId());
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

        final int newAmount = 8;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, newAmount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<Basket> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            Basket.class);

        Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        final Basket basket = putResponse.getBody();
        Assertions.assertEquals(newAmount, basket.getAmount());
        Assertions.assertEquals(existingUserId, basket.getUser().getId());
        Assertions.assertEquals(existingProductId, basket.getProduct().getId());
    }

    @Test
    void addOrUpdateBasketUserNotFound() {
        final int notExistingUserId = 9;
        Mockito.when(userService.findById(notExistingUserId))
            .thenThrow(new UserNotFoundException(notExistingUserId));

        final int existingProductId = 24;
        final int amount = 4;
        AddBasketForm addBasketForm = new AddBasketForm(notExistingUserId, existingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<UserErrorResponse> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            UserErrorResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, putResponse.getStatusCode());
        UserErrorResponse userErrorResponse = putResponse.getBody();
        Assertions.assertEquals(
            String.format("User ID %s not found.", notExistingUserId),
            userErrorResponse.getMessage());
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

        final int amount = 4;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, notExistingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<ProductErrorResponse> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            ProductErrorResponse.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, putResponse.getStatusCode());
        ProductErrorResponse productErrorResponse = putResponse.getBody();
        Assertions.assertEquals(
            String.format("Product ID %s not found.", notExistingProductId),
            productErrorResponse.getMessage());
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

        final int amount = 9;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<BasketErrorResponse> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            BasketErrorResponse.class);
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, putResponse.getStatusCode());
        BasketErrorResponse basketErrorResponse = putResponse.getBody();
        Assertions.assertEquals(
            String.format("Product ID %s has only pieces %s left in stock.",
                existingProductId, currentAmountInStock),
            basketErrorResponse.getMessage());
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

        final int amount = 9;
        AddBasketForm addBasketForm = new AddBasketForm(existingUserId, existingProductId, amount);
        HttpEntity<AddBasketForm> putRequest = new HttpEntity<>(addBasketForm);
        ResponseEntity<BasketErrorResponse> putResponse = testRestTemplate.exchange(
            "/baskets",
            HttpMethod.PUT,
            putRequest,
            BasketErrorResponse.class);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, putResponse.getStatusCode());
        BasketErrorResponse basketErrorResponse = putResponse.getBody();
        Assertions.assertEquals(
            "Basket system is temporarily unavailable. Sorry for your inconvenience.",
            basketErrorResponse.getMessage());
    }
}