package com.example.bakery.users;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(int userId) {
        super(String.format("User ID %s not found.", userId));
    }
}
