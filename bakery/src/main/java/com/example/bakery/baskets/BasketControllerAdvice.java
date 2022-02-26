package com.example.bakery.baskets;

import com.example.bakery.products.ProductErrorResponse;
import com.example.bakery.products.ProductNotFoundException;
import com.example.bakery.users.UserErrorResponse;
import com.example.bakery.users.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BasketControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProductErrorResponse handleProductNotFound(ProductNotFoundException ex) {
        final String errorMessage = ex.getMessage();
        return new ProductErrorResponse(errorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorResponse handleUserNotFound(UserNotFoundException ex) {
        final String errorMessage = ex.getMessage();
        return new UserErrorResponse(errorMessage);
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public BasketErrorResponse handleOutOfStock(OutOfStockException ex) {
        final String errorMessage = ex.getMessage();
        return new BasketErrorResponse(errorMessage);
    }

    @ExceptionHandler(DuplicatedBasketException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BasketErrorResponse handleDuplicatedBasket(DuplicatedBasketException ex) {
        final String errorMessage = "Basket system is temporarily unavailable. Sorry for your inconvenience.";
        return new BasketErrorResponse(errorMessage);
    }
}
