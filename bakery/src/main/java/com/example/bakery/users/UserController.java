package com.example.bakery.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAll() {
        // This GET API was added for testing only.
        // In production, normal users should not be able to call this API.
        return userService.findAll();
    }
}
