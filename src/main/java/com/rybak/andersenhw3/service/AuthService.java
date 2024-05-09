package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.UserDao;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.exception.UserAlreadyExistException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.util.PasswordUtil;

import java.util.UUID;

public class AuthService {

    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(User user) {
        boolean userExists = userDao.existsByEmail(user.getEmail());
        if (userExists) {
            throw new UserAlreadyExistException(String.format("User with email %s already exists", user.getEmail()));
        }

        user.setId(UUID.randomUUID());
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));

        userDao.insertUser(user);

        return user;
    }

    public boolean login(String email, String password) {
        User user = userDao.findUserByEmail(email);
        if(user == null) {
            throw new UserNotFoundException(String.format("User with email %s not found", email));
        }

        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            throw new TaskManagerGlobalException(String.format("Incorrect password for user %s", email), 400);
        }

        return true;
    }

}
