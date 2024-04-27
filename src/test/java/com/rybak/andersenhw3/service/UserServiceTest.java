package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.Role;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class UserServiceTest {

    private final UserService userService = new UserService();
    private final String NAME = "Kirill";
    private final String PASSWORD = "11111111";
    private final String EMAIL = "kirill@test.com";

    @BeforeEach
    public void setup() {
        GlobalStorage.users.add(new User(UUID.randomUUID(), NAME, EMAIL, PASSWORD, Role.DM));
        GlobalStorage.users.add(new User(UUID.randomUUID(), NAME + "1", EMAIL, PASSWORD, Role.PM));
        GlobalStorage.users.add(new User(UUID.randomUUID(), NAME, EMAIL + "2", PASSWORD, Role.QA));
        GlobalStorage.users.add(new User(UUID.randomUUID(), NAME, EMAIL + "3", PASSWORD, Role.DM));
    }

    @AfterEach
    public void teardown() {
        GlobalStorage.users.clear();
    }

    @Test
    void getAllUsers_ShouldSuccessfullyReturnAllUsers() {
        List<User> actual = userService.getAllUsers();

        Assertions.assertEquals(GlobalStorage.users.size(), actual.size());
    }

    @Test
    void getUserById_WhenUUIDIsExists_ShouldReturnUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, NAME, EMAIL + "3", PASSWORD, Role.DM);
        GlobalStorage.users.add(user);

        User actual = userService.getUserById(id);

        Assertions.assertEquals(user, actual);
    }

    @Test
    void getUserById_WhenUUIDNotExists_ShouldThrowUserNotFoundException() {
        UUID id = UUID.randomUUID();
        User user = new User(id, NAME, EMAIL + "3", PASSWORD, Role.DM);
        GlobalStorage.users.add(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(UUID.randomUUID()));
    }

    @Test
    void deleteUserById_WhenUUIDIsExists_ShouldRemoveUserAndReturnTrue() {
        UUID id = UUID.randomUUID();
        User user = new User(id, NAME, EMAIL + "3", PASSWORD, Role.DM);
        GlobalStorage.users.add(user);

        boolean actual = userService.deleteUserById(id);

        Assertions.assertTrue(actual);
    }

    @Test
    void deleteUserById_WhenUUIDIsNotExists_ShouldReturnFalse() {
        UUID id = UUID.randomUUID();
        User user = new User(id, NAME, EMAIL + "3", PASSWORD, Role.DM);
        GlobalStorage.users.add(user);

        boolean actual = userService.deleteUserById(UUID.randomUUID());

        Assertions.assertFalse(actual);
    }

    @Test
    void getUserByEmail_WhenEmailIsExists_ShouldReturnUser() {
        String newEmail = "alex@gmail.com";
        User user = new User(UUID.randomUUID(), NAME, newEmail, PASSWORD, Role.DM);
        GlobalStorage.users.add(user);

        User actual = userService.getUserByEmail(newEmail);

        Assertions.assertEquals(user, actual);
    }

    @Test
    void getUserByEmail_WhenUUIDNotExists_ShouldThrowUserNotFoundException() {
        User user = new User(UUID.randomUUID(), NAME, EMAIL, PASSWORD, Role.DM);
        GlobalStorage.users.add(user);
        String notExistEmail = "alex@gmail.com";

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(notExistEmail));
    }

}