package com.example.bakery.users;

import lombok.Getter;
import lombok.Setter;

public class UserErrorResponse {

    // default constructor is required by Spring Boot
    public UserErrorResponse() {}

    public UserErrorResponse(String message) {
        this.message = message;
    }

    @Getter
    @Setter
    private String message;
}
