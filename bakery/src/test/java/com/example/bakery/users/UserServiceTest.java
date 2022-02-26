package com.example.bakery.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void findByIdSuccess() {
        final int existingUserId = 1;
        User mockUser = new User();
        mockUser.setName("existing user name");
        Mockito.when(userRepository.findById(existingUserId))
            .thenReturn(Optional.of(mockUser));

        UserService userService = new UserService();
        userService.setUserRepository(userRepository);
        User user = userService.findById(existingUserId);

        Assertions.assertEquals("existing user name", user.getName());
    }

    @Test
    void findByIdNotFound() {
        final int notExistingUserId = 998;
        User mockUser = new User();
        mockUser.setName("existing user name");
        Mockito.when(userRepository.findById(notExistingUserId))
            .thenThrow(new UserNotFoundException(notExistingUserId));

        UserService userService = new UserService();
        userService.setUserRepository(userRepository);

        UserNotFoundException exception = Assertions.assertThrows(
            UserNotFoundException.class,
            () -> userService.findById(notExistingUserId));
        Assertions.assertEquals(
            String.format("User ID %s not found.", notExistingUserId),
            exception.getMessage());
    }
}