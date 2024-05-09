package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.UserDao;
import com.rybak.andersenhw3.entity.Role;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.exception.UserAlreadyExistException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.util.PasswordUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private AuthService authService;

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

    @Test
    void register_WhenUserIsNew_ShouldSuccessfullyAddUser() {
        Mockito.when(userDao.existsByEmail(EMAIL)).thenReturn(false);
        Mockito.doNothing().when(userDao).insertUser(Mockito.any(User.class));

        User actual = authService.register(user);

        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getRole(), actual.getRole());
        Assertions.assertEquals(user.getName(), actual.getName());
        Assertions.assertNotNull(actual.getId());
    }

    @Test
    void register_WhenUserExists_ShouldThrowException() {
        Mockito.when(userDao.existsByEmail(EMAIL)).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistException.class, () -> authService.register(user));
    }

    @Test
    void login_WhenPasswordIsIncorrect_ShouldThrowTaskManagerGlobalException() {
        Mockito.when(userDao.findUserByEmail(EMAIL)).thenReturn(user);

        user.setPassword(PasswordUtil.hashPassword(PASSWORD));
        String anotherPassword = PASSWORD + "123";

        Assertions.assertThrows(TaskManagerGlobalException.class, () -> authService.login(EMAIL, anotherPassword));
    }

    @Test
    void login_WhenUserNotExists_ShouldThrowUserNotFoundException() {
        Mockito.when(userDao.findUserByEmail(EMAIL)).thenReturn(null);

        Assertions.assertThrows(UserNotFoundException.class, () -> authService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_WhenDataIsCorrect_ShouldReturnTrue() {
        Mockito.when(userDao.findUserByEmail(EMAIL)).thenReturn(user);
        user.setPassword(PasswordUtil.hashPassword(PASSWORD));

        Assertions.assertTrue(authService.login(EMAIL, PASSWORD));
    }

}