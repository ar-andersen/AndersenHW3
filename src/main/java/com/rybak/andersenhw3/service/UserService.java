package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;

import java.util.List;
import java.util.UUID;

public class UserService {

    public List<User> getAllUsers() {
        return List.copyOf(GlobalStorage.users);
    }

    public User getUserById(UUID id) {
        return GlobalStorage.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s doesn't exists", id)));
    }

    public boolean deleteUserById(UUID id) {
        return GlobalStorage.users.removeIf(user -> user.getId().equals(id));
    }

    public User getUserByEmail(String email) {
        return GlobalStorage.users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email %s doesn't exists", email)));
    }

}
