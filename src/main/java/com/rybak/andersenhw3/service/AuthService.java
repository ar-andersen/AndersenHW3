package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.exception.UserAlreadyExistException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;
import com.rybak.andersenhw3.util.PasswordUtil;

import java.util.List;
import java.util.UUID;

public class AuthService {

    public User register(User user) {
        List<User> users = GlobalStorage.users;

        boolean userExists = GlobalStorage.users.stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (userExists) {
            throw new UserAlreadyExistException(String.format("User with email %s already exists", user.getEmail()));
        }

        user.setId(UUID.randomUUID());
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));

        users.add(user);

        return user;
    }

    public boolean login(String email, String password) {
        List<User> users = GlobalStorage.users;

        User user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", email)));

        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            throw new TaskManagerGlobalException(String.format("Incorrect password for user %s", email), 400);
        }

        return true;
    }

}
