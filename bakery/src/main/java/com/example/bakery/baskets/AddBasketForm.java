package com.example.bakery.baskets;

import lombok.Getter;

public class AddBasketForm {

    @Getter
    private int userId;
    @Getter
    private int productId;
    @Getter
    private int amount;

    public AddBasketForm() {}

    public AddBasketForm(int userId, int productId, int amount) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }
}
