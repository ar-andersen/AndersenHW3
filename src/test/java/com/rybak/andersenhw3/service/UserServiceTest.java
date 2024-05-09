package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.UserDao;
import com.rybak.andersenhw3.entity.Role;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final String NAME = "Kirill";
    private final String PASSWORD = "11111111";
    private final String EMAIL = "kirill@test.com";
    private final List<User> users = new ArrayList<>();

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        users.add(new User(UUID.randomUUID(), NAME, EMAIL, PASSWORD, Role.DM));
        users.add(new User(UUID.randomUUID(), NAME + "1", EMAIL, PASSWORD, Role.PM));
        users.add(new User(UUID.randomUUID(), NAME, EMAIL + "2", PASSWORD, Role.QA));
        users.add(new User(UUID.randomUUID(), NAME, EMAIL + "3", PASSWORD, Role.DM));
    }

    @AfterEach
    public void teardown() {
        users.clear();
    }

    @Test
    void getAllUsers_ShouldSuccessfullyReturnAllUsers() {
        Mockito.when(userDao.getAllUsers()).thenReturn(users);

        List<User> actual = userService.getAllUsers();

        Assertions.assertEquals(users.size(), actual.size());
        Assertions.assertEquals(users, actual);
    }

    @Test
    void getUserById_WhenUUIDIsExists_ShouldReturnUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, NAME, EMAIL + "3", PASSWORD, Role.DM);
        users.add(user);
        Mockito.when(userDao.findUserById(id)).thenReturn(user);

        User actual = userService.getUserById(id);

        Assertions.assertEquals(user, actual);
    }

    @Test
    void getUserById_WhenUUIDNotExists_ShouldThrowUserNotFoundException() {
        UUID id = UUID.randomUUID();
        User user = new User(id, NAME, EMAIL + "3", PASSWORD, Role.DM);
        users.add(user);
        Mockito.when(userDao.findUserById(id)).thenReturn(null);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    void deleteUserById_WhenUUIDIsExists_ShouldRemoveUserAndReturnTrue() {
        UUID id = UUID.randomUUID();
        Mockito.when(userDao.deleteUserById(id)).thenReturn(true);

        boolean actual = userService.deleteUserById(id);

        Assertions.assertTrue(actual);
    }

    @Test
    void deleteUserById_WhenUUIDIsNotExists_ShouldReturnFalse() {
        UUID id = UUID.randomUUID();
        Mockito.when(userDao.deleteUserById(id)).thenReturn(false);

        boolean actual = userService.deleteUserById(id);

        Assertions.assertFalse(actual);
    }

    @Test
    void getUserByEmail_WhenEmailIsExists_ShouldReturnUser() {
        String newEmail = "alex@gmail.com";
        User user = new User(UUID.randomUUID(), NAME, newEmail, PASSWORD, Role.DM);
        Mockito.when(userDao.findUserByEmail(newEmail)).thenReturn(user);

        User actual = userService.getUserByEmail(newEmail);

        Assertions.assertEquals(user, actual);
    }

    @Test
    void getUserByEmail_WhenUUIDNotExists_ShouldThrowUserNotFoundException() {
        String notExistEmail = "alex@gmail.com";
        Mockito.when(userDao.findUserByEmail(notExistEmail)).thenReturn(null);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(notExistEmail));
    }

}