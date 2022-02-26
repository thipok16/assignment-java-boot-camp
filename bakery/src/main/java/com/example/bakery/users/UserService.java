package com.example.bakery.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(int userId) {
        Optional<User> result = userRepository.findById(userId);
        if (result.isPresent()) {
            return result.get();
        }
        throw new UserNotFoundException(userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
