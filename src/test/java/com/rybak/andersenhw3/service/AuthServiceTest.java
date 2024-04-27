package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.Role;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.exception.UserAlreadyExistException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;
import com.rybak.andersenhw3.util.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthServiceTest {

    private final AuthService authService = new AuthService();
    private final String NAME = "Kirill";
    private final String PASSWORD = "11111111";
    private final String EMAIL = "kirill@test.com";
    private final Role ROLE = Role.DEV;
    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setName(NAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRole(ROLE);
    }

    @AfterEach
    public void teardown() {
        GlobalStorage.users.clear();
    }

    @Test
    void register_WhenUserIsNew_ShouldSuccessfullyAddUser() {
        User actual = authService.register(user);

        User expected = GlobalStorage.users.stream()
                .filter(u -> u.getEmail().equals(EMAIL))
                .findFirst().get();

        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getRole(), actual.getRole());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertNotNull(actual.getId());
    }

    @Test
    void register_WhenUserExists_ShouldThrowException() {
        GlobalStorage.users.add(user);

        Assertions.assertThrows(UserAlreadyExistException.class, () -> authService.register(user));
    }

    @Test
    void login_WhenPasswordIsIncorrect_ShouldThrowTaskManagerGlobalException() {
        user.setPassword(PasswordUtil.hashPassword(PASSWORD));
        GlobalStorage.users.add(user);
        String anotherPassword = PASSWORD + "123";

        Assertions.assertThrows(TaskManagerGlobalException.class, () -> authService.login(EMAIL, anotherPassword));
    }

    @Test
    void login_WhenUserNotExists_ShouldThrowUserNotFoundException() {
        GlobalStorage.users.add(user);
        String anotherEmail = PASSWORD + "123";

        Assertions.assertThrows(UserNotFoundException.class, () -> authService.login(anotherEmail, PASSWORD));
    }

}